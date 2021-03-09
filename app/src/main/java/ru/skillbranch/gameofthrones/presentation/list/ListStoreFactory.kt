package ru.skillbranch.gameofthrones.presentation.list

import com.arkivanov.mvikotlin.core.store.*
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import com.arkivanov.mvikotlin.extensions.reaktive.ReaktiveExecutor
import com.badoo.reaktive.coroutinesinterop.singleFromCoroutine
import com.badoo.reaktive.scheduler.ioScheduler
import com.badoo.reaktive.scheduler.mainScheduler
import com.badoo.reaktive.single.map
import com.badoo.reaktive.single.observeOn
import com.badoo.reaktive.single.subscribeOn
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import ru.skillbranch.gameofthrones.AppConfig
import ru.skillbranch.gameofthrones.data.local.cache.CharacterDao
import ru.skillbranch.gameofthrones.data.local.cache.HouseDao
import ru.skillbranch.gameofthrones.data.local.entities.CharacterItem
import ru.skillbranch.gameofthrones.data.toCharacterItem
import ru.skillbranch.gameofthrones.debugLog
import ru.skillbranch.gameofthrones.presentation.list.ListStore.*

internal class ListStoreFactory(
  private val storeFactory: StoreFactory,
  private val houseDao: HouseDao,
  private val characterDao: CharacterDao
) {

  fun create(): ListStore =
    object : ListStore, Store<Intent, State, Label> by storeFactory.create(
      name = "ListStore",
      initialState = State(),
      bootstrapper = SimpleBootstrapper(Unit),
      executorFactory = ::createExecutor,
      reducer = ReducerImpl
    ) {}

  private fun createExecutor(): Executor<Intent, Unit, State, Result, Label> = ExecutorImpl()

  private inner class ExecutorImpl : ReaktiveExecutor<Intent, Unit, State, Result, Label>() {

    override fun executeAction(action: Unit, getState: () -> State) {
      singleFromCoroutine {
        AppConfig.NEED_HOUSES.map { houseName ->
          async {
            val house = houseDao.getByName(houseName)
            val characters = house?.id?.let { houseId ->
              characterDao.findByHouseId(houseId)?.map { it.toCharacterItem(houseName) }
            }
            houseName to (characters ?: emptyList())
          }
        }.awaitAll()
      }
        .subscribeOn(ioScheduler)
        .map { it.toMap() }
        .map(Result::Loaded)
        .observeOn(mainScheduler)
        .subscribeScoped(
          isThreadLocal = true,
          onSuccess = ::onSuccessLoading,
          onError = ::handleError
        )
    }

    fun onSuccessLoading(result: Result.Loaded) {
      debugLog("data loaded")
      result.data
      dispatch(result)
    }

    override fun executeIntent(intent: Intent, getState: () -> State) {
      when (intent) {
        else -> null
      }.let {}
    }

    fun handleError(throwable: Throwable) {
      debugLog(throwable.message)
      dispatch(Result.Error(throwable.message!!))
    }
  }

  sealed class Result : JvmSerializable {
    object Loading : Result()
    data class Loaded(val data: Map<String, List<CharacterItem>>) : Result()
    data class Error(val message: String) : Result()
  }

  private object ReducerImpl : Reducer<State, Result> {
    override fun State.reduce(result: Result): State =
      when (result) {
        is Result.Loaded -> copy(data = result.data, error = null, loading = false)
        is Result.Error -> copy(error = result.message, loading = false)
        Result.Loading -> copy(error = null, loading = true)
      }
  }
}