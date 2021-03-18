package ru.skillbranch.gameofthrones.presentation.houses

import com.arkivanov.mvikotlin.core.binder.BinderLifecycleMode
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.lifecycle.doOnDestroy
import com.arkivanov.mvikotlin.extensions.reaktive.bind
import com.arkivanov.mvikotlin.extensions.reaktive.events
import com.arkivanov.mvikotlin.extensions.reaktive.labels
import com.arkivanov.mvikotlin.extensions.reaktive.states
import com.badoo.reaktive.observable.mapNotNull
import com.badoo.reaktive.subject.Relay
import com.badoo.reaktive.subject.publish.PublishSubject
import ru.skillbranch.gameofthrones.presentation.houses.HousesListController.Dependencies
import ru.skillbranch.gameofthrones.presentation.houses.HousesListController.Input
import ru.skillbranch.gameofthrones.presentation.search.SearchStoreFactory
import ru.skillbranch.gameofthrones.presentation.search.SearchView

class HousesListControllerImpl(
  private val dependencies: Dependencies
) : HousesListController {

  private val inputRelay: Relay<Input> = PublishSubject()
  override val input: (Input) -> Unit = inputRelay::onNext

  private val listStore =
    dependencies.instanceKeeper.getStore {
      HousesListStoreFactory(
        storeFactory = dependencies.storeFactory,
        houseDao = dependencies.houseDao,
        characterDao = dependencies.characterDao
      ).create()
    }

  private val searchStore =
    dependencies.instanceKeeper.getStore {
      SearchStoreFactory(
        storeFactory = dependencies.storeFactory
      ).create()
    }

  init {
    bind(dependencies.lifecycle, BinderLifecycleMode.CREATE_DESTROY) {
      inputRelay.mapNotNull(housesListInputToListIntent) bindTo listStore
      searchStore.labels.mapNotNull(searchLabelToHousesListIntent) bindTo listStore
    }

    dependencies.lifecycle.doOnDestroy(listStore::dispose)
  }

  override fun onViewCreated(
    housesListView: HousesListView,
    searchView: SearchView,
    viewLifecycle: Lifecycle
  ) {
    bind(viewLifecycle, BinderLifecycleMode.CREATE_DESTROY) {
      housesListView.events.mapNotNull(housesListEventToListIntent) bindTo listStore
      searchView.events.mapNotNull(searchEventToSearchIntent) bindTo searchStore
    }

    bind(viewLifecycle, BinderLifecycleMode.START_STOP) {
      listStore.states.mapNotNull(housesListStateToListModel) bindTo housesListView
      searchStore.states.mapNotNull(searchStateToSearchModel) bindTo searchView
      housesListView.events.mapNotNull(housesListEventToOutput) bindTo dependencies.output
    }
  }
}