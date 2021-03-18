package ru.skillbranch.gameofthrones.presentation.characters

val charactersListStateToListModel: CharactersListStore.State.() -> CharactersListView.Model? = {
  CharactersListView.Model(
    loading = loading,
    characters = data,
    error = error,
  )
}

val charactersListInputToListIntent: CharactersListController.Input.() -> CharactersListStore.Intent? =
  {
    when (this) {
      else -> throw NotImplementedError("not implemented charactersListInputToListIntent")
    }
  }

val charactersListEventToListIntent: CharactersListView.Event.() -> CharactersListStore.Intent? = {
  when (this) {
    is CharactersListView.Event.ItemClicked -> null
    else -> throw NotImplementedError("not implemented charactersListEventToListIntent")
  }
}

val charactersListEventToOutput: CharactersListView.Event.() -> CharactersListController.Output? = {
  when (this) {
    is CharactersListView.Event.ItemClicked -> CharactersListController.Output.OpenParentInfo(item.id)
    else -> throw NotImplementedError("not implemented housesListEventToOutput")
  }
}