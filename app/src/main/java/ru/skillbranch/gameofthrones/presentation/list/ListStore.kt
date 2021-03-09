package ru.skillbranch.gameofthrones.presentation.list

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import ru.skillbranch.gameofthrones.data.local.entities.CharacterItem
import ru.skillbranch.gameofthrones.presentation.list.ListStore.*


interface ListStore : Store<Intent, State, Label> {

  sealed class Intent : JvmSerializable {
  }

  data class State(
    val loading: Boolean = false,
    val data: Map<String, List<CharacterItem>> = emptyMap(),
    val error: String? = null
  ) : JvmSerializable

  sealed class Label {

  }

}