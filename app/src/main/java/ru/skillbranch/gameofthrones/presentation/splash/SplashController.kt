package ru.skillbranch.gameofthrones.presentation.splash

import com.arkivanov.mvikotlin.core.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.keepers.instancekeeper.InstanceKeeper
import ru.skillbranch.gameofthrones.repositories.RootRepository

interface SplashController {

  fun onViewCreated(
    splashView: SplashView,
    viewLifecycle: Lifecycle
  )

  interface Dependencies {
    val rootRepository: RootRepository
    val storeFactory: StoreFactory
    val lifecycle: Lifecycle
    val instanceKeeper: InstanceKeeper
    val output: (Output) -> Unit
  }

  sealed class Output {
    object DataLoaded: Output()
  }
}

class SplashControllerDependencies(
  override val storeFactory: StoreFactory,
  override val lifecycle: Lifecycle,
  override val instanceKeeper: InstanceKeeper,
  override val output: (SplashController.Output) -> Unit,
  override val rootRepository: RootRepository
) : SplashController.Dependencies