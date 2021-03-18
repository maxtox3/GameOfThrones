package ru.skillbranch.gameofthrones.presentation.characters

import com.arkivanov.mvikotlin.core.view.MviView
import ru.skillbranch.gameofthrones.data.local.entities.CharacterItem
import ru.skillbranch.gameofthrones.presentation.characters.CharactersListView.Event
import ru.skillbranch.gameofthrones.presentation.characters.CharactersListView.Model

interface CharactersListView : MviView<Model, Event> {

  data class Model(
    val loading: Boolean,
    val characters: List<CharacterItem> = emptyList(),
    val error: String? = null
  )

  sealed class Event {
    data class ItemClicked(val item: CharacterItem): Event()
  }

}