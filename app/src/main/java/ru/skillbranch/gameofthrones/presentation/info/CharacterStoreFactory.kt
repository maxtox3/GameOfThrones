package ru.skillbranch.gameofthrones.presentation.info

import com.arkivanov.mvikotlin.core.store.*
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import com.arkivanov.mvikotlin.extensions.reaktive.ReaktiveExecutor
import com.badoo.reaktive.coroutinesinterop.maybeFromCoroutine
import com.badoo.reaktive.maybe.map
import com.badoo.reaktive.maybe.observeOn
import com.badoo.reaktive.maybe.subscribeOn
import com.badoo.reaktive.scheduler.ioScheduler
import com.badoo.reaktive.scheduler.mainScheduler
import ru.skillbranch.gameofthrones.data.local.cache.CharacterDao
import ru.skillbranch.gameofthrones.data.local.cache.HouseDao
import ru.skillbranch.gameofthrones.data.local.entities.CharacterFull
import ru.skillbranch.gameofthrones.data.toCharacterFull
import ru.skillbranch.gameofthrones.data.toRelativeCharacter
import ru.skillbranch.gameofthrones.debugLog
import ru.skillbranch.gameofthrones.parseId
import ru.skillbranch.gameofthrones.presentation.info.CharacterStore.*

internal class CharacterStoreFactory(
  private val storeFactory: StoreFactory,
  private val houseDao: HouseDao,
  private val characterDao: CharacterDao,
  private val characterId: String
) {

  fun create(): CharacterStore =
    object : CharacterStore, Store<Intent, State, Label> by storeFactory.create(
      name = "CharacterStore",
      initialState = State(),
      bootstrapper = SimpleBootstrapper(Unit),
      executorFactory = ::createExecutor,
      reducer = ReducerImpl
    ) {}

  private fun createExecutor(): Executor<Intent, Unit, State, Result, Label> = ExecutorImpl()

  private inner class ExecutorImpl : ReaktiveExecutor<Intent, Unit, State, Result, Label>() {

    override fun executeAction(action: Unit, getState: () -> State) {
      maybeFromCoroutine {
        characterDao.getById(characterId)?.let { character ->
          val house = houseDao.getById(character.houseId)
          val father = characterDao.getById(parseId(character.father))?.toRelativeCharacter(house)
          val mother = characterDao.getById(parseId(character.mother))?.toRelativeCharacter(house)
          character.toCharacterFull(house, father = father, mother = mother)
        } ?: throw Exception("Character not found")
      }
        .subscribeOn(ioScheduler)
        .map(Result::Loaded)
        .observeOn(mainScheduler)
        .subscribeScoped(
          isThreadLocal = true,
          onSuccess = ::dispatch,
          onError = ::handleError
        )
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
    data class Loaded(val data: CharacterFull) : Result()
    data class Error(val message: String) : Result()
  }

  private object ReducerImpl : Reducer<State, Result> {
    override fun State.reduce(result: Result): State =
      when (result) {
        is Result.Loaded -> copy(character = result.data, error = null, loading = false)
        is Result.Error -> copy(error = result.message, loading = false)
        Result.Loading -> copy(error = null, loading = true)
      }
  }
}