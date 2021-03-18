package ru.skillbranch.gameofthrones.presentation.houses

import com.arkivanov.mvikotlin.core.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.keepers.instancekeeper.InstanceKeeper
import ru.skillbranch.gameofthrones.data.local.cache.CharacterDao
import ru.skillbranch.gameofthrones.data.local.cache.HouseDao
import ru.skillbranch.gameofthrones.presentation.search.SearchView

interface HousesListController {

  val input: (Input) -> Unit

  fun onViewCreated(
    housesListView: HousesListView,
    searchView: SearchView,
    viewLifecycle: Lifecycle
  )

  interface Dependencies {
    val houseDao: HouseDao
    val characterDao: CharacterDao
    val storeFactory: StoreFactory
    val lifecycle: Lifecycle
    val instanceKeeper: InstanceKeeper
    val housesOutput: (Output) -> Unit
  }

  sealed class Output {
    data class OpenCharacterInfo(val id: String) : Output()
  }

  sealed class Input {

  }
}