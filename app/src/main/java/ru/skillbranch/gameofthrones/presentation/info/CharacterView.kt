package ru.skillbranch.gameofthrones.presentation.info

import com.arkivanov.mvikotlin.core.view.MviView
import ru.skillbranch.gameofthrones.data.local.entities.CharacterFull
import ru.skillbranch.gameofthrones.data.local.entities.CharacterItem
import ru.skillbranch.gameofthrones.data.local.entities.RelativeCharacter
import ru.skillbranch.gameofthrones.presentation.info.CharacterView.Event
import ru.skillbranch.gameofthrones.presentation.info.CharacterView.Model

interface CharacterView : MviView<Model, Event> {

  data class Model(
    val loading: Boolean,
    val error: String? = null,
    val character: CharacterFull? = null
  )

  sealed class Event {
    data class RelativeCharacterClicker(val relative: RelativeCharacter) : Event()
    data class ItemClicked(val characterItem: CharacterItem) : Event()
  }

}