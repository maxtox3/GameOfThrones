package ru.skillbranch.gameofthrones.presentation.characters

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import ru.skillbranch.gameofthrones.data.local.entities.CharacterItem
import ru.skillbranch.gameofthrones.presentation.characters.CharactersListStore.*

interface CharactersListStore : Store<Intent, State, Label> {

  sealed class Intent : JvmSerializable {
  }

  data class State(
    val loading: Boolean = false,
    val data: List<CharacterItem> = emptyList(),
    val error: String? = null
  ) : JvmSerializable

  sealed class Label {

  }

}