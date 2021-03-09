package ru.skillbranch.gameofthrones.presentation.list

import com.arkivanov.mvikotlin.core.view.MviView
import ru.skillbranch.gameofthrones.data.local.entities.CharacterItem
import ru.skillbranch.gameofthrones.presentation.list.ListView.Event
import ru.skillbranch.gameofthrones.presentation.list.ListView.Model

interface ListView : MviView<Model, Event> {

  data class Model(
    val loading: Boolean,
    val tabs: List<String> = emptyList(),
    val error: String? = null,
    val housesWithCharacters: Map<String, List<CharacterItem>>
  )

  sealed class Event {
    data class ItemClicked(val characterItem: CharacterItem) : Event()

  }

}