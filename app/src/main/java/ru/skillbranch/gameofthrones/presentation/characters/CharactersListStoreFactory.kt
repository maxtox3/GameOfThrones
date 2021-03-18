package ru.skillbranch.gameofthrones.presentation.characters

import com.arkivanov.mvikotlin.core.store.*
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import com.arkivanov.mvikotlin.extensions.reaktive.ReaktiveExecutor
import com.badoo.reaktive.coroutinesinterop.singleFromCoroutine
import com.badoo.reaktive.scheduler.ioScheduler
import com.badoo.reaktive.scheduler.mainScheduler
import com.badoo.reaktive.single.map
import com.badoo.reaktive.single.observeOn
import com.badoo.reaktive.single.subscribeOn
import ru.skillbranch.gameofthrones.data.local.cache.CharacterDao
import ru.skillbranch.gameofthrones.data.local.cache.HouseDao
import ru.skillbranch.gameofthrones.data.local.entities.CharacterItem
import ru.skillbranch.gameofthrones.data.toCharacterItem
import ru.skillbranch.gameofthrones.debugLog

internal class CharactersListStoreFactory(
  private val storeFactory: StoreFactory,
  private val houseDao: HouseDao,
  private val characterDao: CharacterDao,
  private val houseId: String
) {

  fun create(): CharactersListStore =
    object : CharactersListStore,
      Store<CharactersListStore.Intent, CharactersListStore.State, CharactersListStore.Label> by storeFactory.create(
        name = "CharactersListStore_$houseId",
        initialState = CharactersListStore.State(),
        bootstrapper = SimpleBootstrapper(Unit),
        executorFactory = ::createExecutor,
        reducer = ReducerImpl
      ) {}

  private fun createExecutor(): Executor<CharactersListStore.Intent, Unit, CharactersListStore.State, Result, CharactersListStore.Label> =
    ExecutorImpl()

  private inner class ExecutorImpl :
    ReaktiveExecutor<CharactersListStore.Intent, Unit, CharactersListStore.State, Result, CharactersListStore.Label>() {

    override fun executeAction(action: Unit, getState: () -> CharactersListStore.State) {
      singleFromCoroutine {
        val house = houseDao.getById(houseId) ?: throw NullPointerException()
        characterDao.findByHouseId(houseId)?.map { it.toCharacterItem(house.name) }
          ?: throw NullPointerException()
      }
        .subscribeOn(ioScheduler)
        .map(Result::Loaded)
        .observeOn(mainScheduler)
        .subscribeScoped(
          isThreadLocal = true,
          onSuccess = ::onSuccessLoading,
          onError = ::handleError
        )
    }

    fun onSuccessLoading(result: Result.Loaded) {
      debugLog("characters loaded")
      result.data
      dispatch(result)
    }

    override fun executeIntent(
      intent: CharactersListStore.Intent,
      getState: () -> CharactersListStore.State
    ) {
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
    data class Loaded(val data: List<CharacterItem>) : Result()
    data class Error(val message: String) : Result()
  }

  private object ReducerImpl : Reducer<CharactersListStore.State, Result> {
    override fun CharactersListStore.State.reduce(result: Result): CharactersListStore.State =
      when (result) {
        is Result.Loaded -> copy(data = result.data, error = null, loading = false)
        is Result.Error -> copy(error = result.message, loading = false)
        Result.Loading -> copy(error = null, loading = true)
      }
  }
}