package ru.skillbranch.gameofthrones.presentation.search

import com.arkivanov.mvikotlin.core.store.Executor
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import com.arkivanov.mvikotlin.extensions.reaktive.ReaktiveExecutor
import ru.skillbranch.gameofthrones.presentation.search.SearchStore.*

internal class SearchStoreFactory(
  private val storeFactory: StoreFactory
) {

  fun create(): SearchStore =
    object : SearchStore, Store<Intent, State, Label> by storeFactory.create(
      name = "SearchStore",
      initialState = State(),
      executorFactory = ::createExecutor,
      reducer = ReducerImpl
    ) {}

  private fun createExecutor(): Executor<Intent, Unit, State, Result, Label> = ExecutorImpl()

  private inner class ExecutorImpl : ReaktiveExecutor<Intent, Unit, State, Result, Label>() {

    override fun executeIntent(intent: Intent, getState: () -> State) {
      when (intent) {
        is Intent.ChangeText -> handleTextChange(intent.text)
      }.let {}
    }

    private fun handleTextChange(text: String) {
      dispatch(Result.TextChanged(text))
      publish(Label.FilterChanged(text))
    }
  }

  private sealed class Result : JvmSerializable {
    data class TextChanged(val text: String) : Result()
  }

  private object ReducerImpl : Reducer<State, Result> {
    override fun State.reduce(result: Result): State =
      when (result) {
        is Result.TextChanged -> copy(text = result.text)
      }
  }
}