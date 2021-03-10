package ru.skillbranch.gameofthrones.presentation.info

import com.arkivanov.mvikotlin.core.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.keepers.instancekeeper.InstanceKeeper
import ru.skillbranch.gameofthrones.data.local.cache.CharacterDao
import ru.skillbranch.gameofthrones.data.local.cache.HouseDao

interface CharacterController {

  fun onViewCreated(
    characterView: CharacterView,
    viewLifecycle: Lifecycle
  )

  interface Dependencies {
    val houseDao: HouseDao
    val characterDao: CharacterDao
    val storeFactory: StoreFactory
    val lifecycle: Lifecycle
    val instanceKeeper: InstanceKeeper
    val output: (Output) -> Unit
    val characterId: String
  }

  sealed class Output {
    object DataLoaded : Output()
  }
}

class CharacterControllerDependencies(
  override val storeFactory: StoreFactory,
  override val lifecycle: Lifecycle,
  override val instanceKeeper: InstanceKeeper,
  override val output: (CharacterController.Output) -> Unit,
  override val houseDao: HouseDao,
  override val characterDao: CharacterDao,
  override val characterId: String
) : CharacterController.Dependencies