package ru.skillbranch.gameofthrones.presentation.list

import ru.skillbranch.gameofthrones.presentation.search.SearchStore
import ru.skillbranch.gameofthrones.presentation.search.SearchView
import ru.skillbranch.gameofthrones.shortenHouseName

val listStateToListModel: ListStore.State.() -> ListView.Model? = {
  ListView.Model(
    loading = loading,
    tabs = data.keys.map { shortenHouseName(it) },
    error = error,
    housesWithCharacters = data
  )
}

val listInputToListIntent: ListController.Input.() -> ListStore.Intent? = {
  when (this) {

    else -> throw NotImplementedError("not implemented listInputToListIntent")
  }
}

val searchLabelToListIntent: SearchStore.Label.() -> ListStore.Intent? = {
  when (this) {

    else -> throw NotImplementedError("not implemented searchLabelToListIntent")
  }
}

val listEventToListIntent: ListView.Event.() -> ListStore.Intent? = {
  when (this) {
    is ListView.Event.ItemClicked -> null
    else -> throw NotImplementedError("not implemented listEventToListIntent")
  }
}

val searchEventToSearchIntent: SearchView.Event.() -> SearchStore.Intent? = {
  when (this) {

    else -> throw NotImplementedError("not implemented searchEventToSearchIntent")
  }
}

val searchStateToSearchModel: SearchStore.State.() -> SearchView.Model? = {
  SearchView.Model(
    text = text
  )
}

val listEventToOutput: ListView.Event.() -> ListController.Output? = {
  when (this) {
    is ListView.Event.ItemClicked -> ListController.Output.OpenCharacterInfo(characterItem.id)
    else -> throw NotImplementedError("not implemented listEventToOutput")
  }
}

