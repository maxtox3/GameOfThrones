package ru.skillbranch.gameofthrones.presentation.search

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import ru.skillbranch.gameofthrones.presentation.search.SearchStore.*

interface SearchStore : Store<Intent, State, Label> {

  sealed class Intent : JvmSerializable {
    data class ChangeText(val text: String) : Intent()
  }

  data class State(
    val text: String = ""
  ) : JvmSerializable

  sealed class Label {
    data class FilterChanged(val filter: String) : Label()
  }

}