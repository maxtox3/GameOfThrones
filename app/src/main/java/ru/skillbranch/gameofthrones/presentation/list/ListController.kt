package ru.skillbranch.gameofthrones.presentation.list

import com.arkivanov.mvikotlin.core.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.keepers.instancekeeper.InstanceKeeper
import ru.skillbranch.gameofthrones.data.local.cache.CharacterDao
import ru.skillbranch.gameofthrones.data.local.cache.HouseDao
import ru.skillbranch.gameofthrones.presentation.search.SearchView

interface ListController {

  val input: (Input) -> Unit

  fun onViewCreated(
    listView: ListView,
    searchView: SearchView,
    viewLifecycle: Lifecycle
  )

  interface Dependencies {
    val houseDao: HouseDao
    val characterDao: CharacterDao
    val storeFactory: StoreFactory
    val lifecycle: Lifecycle
    val instanceKeeper: InstanceKeeper
    val output: (Output) -> Unit
  }

  sealed class Output {
    object DataLoaded : Output()
  }

  sealed class Input {

  }
}

class ListControllerDependencies(
  override val storeFactory: StoreFactory,
  override val lifecycle: Lifecycle,
  override val instanceKeeper: InstanceKeeper,
  override val output: (ListController.Output) -> Unit,
  override val houseDao: HouseDao,
  override val characterDao: CharacterDao
) : ListController.Dependencies