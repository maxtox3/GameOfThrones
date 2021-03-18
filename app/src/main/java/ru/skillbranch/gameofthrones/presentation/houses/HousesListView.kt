package ru.skillbranch.gameofthrones.presentation.houses

import com.arkivanov.mvikotlin.core.view.MviView
import ru.skillbranch.gameofthrones.data.local.entities.CharacterItem
import ru.skillbranch.gameofthrones.data.local.entities.House
import ru.skillbranch.gameofthrones.presentation.houses.HousesListView.Event
import ru.skillbranch.gameofthrones.presentation.houses.HousesListView.Model

interface HousesListView : MviView<Model, Event> {

  data class Model(
    val loading: Boolean,
    val houses: List<House> = emptyList(),
    val error: String? = null
  )

  sealed class Event {
  }

}