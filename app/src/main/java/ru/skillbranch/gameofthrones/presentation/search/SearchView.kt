package ru.skillbranch.gameofthrones.presentation.search

import com.arkivanov.mvikotlin.core.view.MviView
import ru.skillbranch.gameofthrones.presentation.search.SearchView.Event
import ru.skillbranch.gameofthrones.presentation.search.SearchView.Model

interface SearchView : MviView<Model, Event> {

  data class Model(
    val text: String? = ""
  )

  sealed class Event {

  }

}