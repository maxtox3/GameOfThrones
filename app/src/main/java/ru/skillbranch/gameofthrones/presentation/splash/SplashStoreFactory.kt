package ru.skillbranch.gameofthrones.presentation.splash

import com.arkivanov.mvikotlin.core.store.*
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import com.arkivanov.mvikotlin.extensions.reaktive.ReaktiveExecutor
import com.badoo.reaktive.scheduler.ioScheduler
import com.badoo.reaktive.scheduler.mainScheduler
import com.badoo.reaktive.single.map
import com.badoo.reaktive.single.observeOn
import com.badoo.reaktive.single.singleFromFunction
import com.badoo.reaktive.single.subscribeOn
import ru.skillbranch.gameofthrones.AppConfig
import ru.skillbranch.gameofthrones.data.remote.res.CharacterRes
import ru.skillbranch.gameofthrones.data.remote.res.HouseRes
import ru.skillbranch.gameofthrones.debugLog
import ru.skillbranch.gameofthrones.presentation.splash.SplashStore.*
import ru.skillbranch.gameofthrones.repositories.RootRepository

internal class SplashStoreFactory(
  private val storeFactory: StoreFactory,
  private val rootRepository: RootRepository
) {

  fun create(): SplashStore =
    object : SplashStore, Store<Intent, State, Label> by storeFactory.create(
      name = "SplashStore",
      initialState = State(),
      bootstrapper = SimpleBootstrapper(Unit),
      executorFactory = ::createExecutor,
      reducer = ReducerImpl
    ) {}

  private fun createExecutor(): Executor<Intent, Unit, State, Result, Label> = ExecutorImpl()

  private inner class ExecutorImpl : ReaktiveExecutor<Intent, Unit, State, Result, Label>() {

    override fun executeAction(action: Unit, getState: () -> State) {
      singleFromFunction {
        rootRepository.getAllHouses { list ->
          rootRepository.insertHouses(list) {}

          rootRepository.getNeedHouses(*AppConfig.NEED_HOUSES) { houses ->
            val typedArray = houses.map { it.name }.toTypedArray()

            rootRepository.getNeedHouseWithCharacters(*typedArray) { listOfPairs ->
              listOfPairs.forEach { pair: Pair<HouseRes, List<CharacterRes>> ->
                rootRepository.insertCharacters(pair.second) {}
              }
            }
          }
        }
      }
        .subscribeOn(ioScheduler)
        .map { Result.Loaded }
        .observeOn(mainScheduler)
        .subscribeScoped(
          isThreadLocal = true,
          onSuccess = ::onSuccessLoading,
          onError = ::handleError
        )
    }

    fun onSuccessLoading(result: Result) {
      debugLog("data loaded")
      dispatch(result)
      publish(Label.DataLoaded)
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
    object Loaded : Result()
    data class Error(val message: String) : Result()
  }

  private object ReducerImpl : Reducer<State, Result> {
    override fun State.reduce(result: Result): State =
      when (result) {
        is Result.Loaded -> copy(loaded = true, error = null, loading = false)
        is Result.Error -> copy(loaded = false, error = result.message, loading = false)
        Result.Loading -> copy(loaded = false, error = null, loading = true)
      }
  }
}