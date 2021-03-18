package ru.skillbranch.gameofthrones.presentation.characters

import com.arkivanov.mvikotlin.core.binder.BinderLifecycleMode
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.lifecycle.doOnDestroy
import com.arkivanov.mvikotlin.extensions.reaktive.bind
import com.arkivanov.mvikotlin.extensions.reaktive.events
import com.arkivanov.mvikotlin.extensions.reaktive.states
import com.badoo.reaktive.observable.mapNotNull
import com.badoo.reaktive.subject.Relay
import com.badoo.reaktive.subject.publish.PublishSubject

class CharactersListControllerImpl(
  private val dependencies: CharactersListController.Dependencies
) : CharactersListController {

  private val inputRelay: Relay<CharactersListController.Input> = PublishSubject()
  override val input: (CharactersListController.Input) -> Unit = inputRelay::onNext

  private val listStore =
    dependencies.instanceKeeper.getStore {
      CharactersListStoreFactory(
        storeFactory = dependencies.storeFactory,
        houseDao = dependencies.houseDao,
        characterDao = dependencies.characterDao,
        houseId = dependencies.houseId
      ).create()
    }

  init {
    bind(dependencies.lifecycle, BinderLifecycleMode.CREATE_DESTROY) {
      inputRelay.mapNotNull(charactersListInputToListIntent) bindTo listStore
    }

    dependencies.lifecycle.doOnDestroy(listStore::dispose)
  }

  override fun onViewCreated(
    listView: CharactersListView,
    viewLifecycle: Lifecycle
  ) {
    bind(viewLifecycle, BinderLifecycleMode.CREATE_DESTROY) {
      listView.events.mapNotNull(charactersListEventToListIntent) bindTo listStore
    }

    bind(viewLifecycle, BinderLifecycleMode.START_STOP) {
      listStore.states.mapNotNull(charactersListStateToListModel) bindTo listView
      listView.events.mapNotNull(charactersListEventToOutput) bindTo dependencies.charactersOutput
    }
  }
}