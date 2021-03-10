package ru.skillbranch.gameofthrones.presentation.info

import com.arkivanov.mvikotlin.core.binder.BinderLifecycleMode
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.lifecycle.doOnDestroy
import com.arkivanov.mvikotlin.extensions.reaktive.bind
import com.arkivanov.mvikotlin.extensions.reaktive.events
import com.arkivanov.mvikotlin.extensions.reaktive.states
import com.badoo.reaktive.observable.mapNotNull

class CharacterControllerImpl(
  private val dependencies: CharacterController.Dependencies
) : CharacterController {

  private val characterStore =
    dependencies.instanceKeeper.getStore {
      CharacterStoreFactory(
        storeFactory = dependencies.storeFactory,
        characterId = dependencies.characterId,
        characterDao = dependencies.characterDao,
        houseDao = dependencies.houseDao
      ).create()
    }

  init {
    dependencies.lifecycle.doOnDestroy(characterStore::dispose)
  }

  override fun onViewCreated(characterView: CharacterView, viewLifecycle: Lifecycle) {
    bind(viewLifecycle, BinderLifecycleMode.CREATE_DESTROY) {
      characterView.events.mapNotNull(characterEventToCharacterIntent) bindTo characterStore
    }

    bind(viewLifecycle, BinderLifecycleMode.START_STOP) {
      characterStore.states.mapNotNull(characterStateToCharacterModel) bindTo characterView
      characterView.events.mapNotNull(characterEventToOutput) bindTo dependencies.output
    }
  }
}