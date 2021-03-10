package ru.skillbranch.gameofthrones.presentation.info

val characterStateToCharacterModel: CharacterStore.State.() -> CharacterView.Model? = {
  CharacterView.Model(
    loading = loading,
    error = error,
    character = character
  )
}

val characterEventToCharacterIntent: CharacterView.Event.() -> CharacterStore.Intent? = {
  when (this) {

    else -> throw NotImplementedError("not implemented characterEventToCharacterIntent")
  }
}

val characterEventToOutput: CharacterView.Event.() -> CharacterController.Output? = {
  when (this) {

    else -> throw NotImplementedError("not implemented characterEventToOutput")
  }
}