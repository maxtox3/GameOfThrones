package ru.skillbranch.gameofthrones.presentation.info

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import ru.skillbranch.gameofthrones.data.local.entities.CharacterFull
import ru.skillbranch.gameofthrones.presentation.info.CharacterStore.*

interface CharacterStore : Store<Intent, State, Label> {

  sealed class Intent : JvmSerializable {
  }

  data class State(
    val loading: Boolean = false,
    val character: CharacterFull? = null,
    val error: String? = null
  ) : JvmSerializable

  sealed class Label {

  }

}