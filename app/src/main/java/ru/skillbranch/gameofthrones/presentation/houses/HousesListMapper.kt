package ru.skillbranch.gameofthrones.presentation.houses

import ru.skillbranch.gameofthrones.presentation.search.SearchStore
import ru.skillbranch.gameofthrones.presentation.search.SearchView
import ru.skillbranch.gameofthrones.shortenHouseName

val housesListStateToListModel: HousesListStore.State.() -> HousesListView.Model? = {
  HousesListView.Model(
    loading = loading,
    houses = data,
    error = error,
  )
}

val searchStateToSearchModel: SearchStore.State.() -> SearchView.Model? = {
  SearchView.Model(
    text = text
  )
}

val housesListInputToListIntent: HousesListController.Input.() -> HousesListStore.Intent? = {
  when (this) {

    else -> throw NotImplementedError("not implemented housesListInputToListIntent")
  }
}

val searchLabelToHousesListIntent: SearchStore.Label.() -> HousesListStore.Intent? = {
  when (this) {

    else -> throw NotImplementedError("not implemented searchLabelToListIntent")
  }
}

val housesListEventToListIntent: HousesListView.Event.() -> HousesListStore.Intent? = {
  when (this) {
    else -> throw NotImplementedError("not implemented housesListEventToListIntent")
  }
}

val searchEventToSearchIntent: SearchView.Event.() -> SearchStore.Intent? = {
  when (this) {

    else -> throw NotImplementedError("not implemented searchEventToSearchIntent")
  }
}

val housesListEventToOutput: HousesListView.Event.() -> HousesListController.Output? = {
  when (this) {
    else -> throw NotImplementedError("not implemented housesListEventToOutput")
  }
}

