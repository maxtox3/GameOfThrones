package ru.skillbranch.gameofthrones.presentation.characters

import com.arkivanov.mvikotlin.core.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.keepers.instancekeeper.InstanceKeeper
import ru.skillbranch.gameofthrones.data.local.cache.CharacterDao
import ru.skillbranch.gameofthrones.data.local.cache.HouseDao

interface CharactersListController {

  val input: (Input) -> Unit

  fun onViewCreated(
    listView: CharactersListView,
    viewLifecycle: Lifecycle
  )

  interface Dependencies {
    val characterDao: CharacterDao
    val houseDao: HouseDao
    val storeFactory: StoreFactory
    val lifecycle: Lifecycle
    val instanceKeeper: InstanceKeeper
    val charactersOutput: (Output) -> Unit
    val houseId: String
  }

  sealed class Output {
    data class OpenParentInfo(val parentId: String) : Output()
  }

  sealed class Input {

  }
}