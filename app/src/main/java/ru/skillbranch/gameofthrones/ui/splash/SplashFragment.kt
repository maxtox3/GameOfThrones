package ru.skillbranch.gameofthrones.ui.splash

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.arkivanov.mvikotlin.core.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.lifecycle.asMviLifecycle
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.keepers.instancekeeper.InstanceKeeper
import com.arkivanov.mvikotlin.keepers.instancekeeper.getInstanceKeeper
import ru.skillbranch.gameofthrones.R
import ru.skillbranch.gameofthrones.presentation.splash.SplashController
import ru.skillbranch.gameofthrones.presentation.splash.SplashControllerImpl
import ru.skillbranch.gameofthrones.repositories.RootRepository

class SplashFragment(
  private val dependencies: Dependencies
) : Fragment(R.layout.splash) {

  private lateinit var controller: SplashController

  private fun createController(lifecycle: Lifecycle): SplashController {
    val controllerDependencies =
      object : SplashController.Dependencies, Dependencies by dependencies {
        override val instanceKeeper: InstanceKeeper = getInstanceKeeper()
        override val output: (SplashController.Output) -> Unit = splashOutput
        override val lifecycle: Lifecycle = lifecycle
      }

    return SplashControllerImpl(controllerDependencies)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    controller = createController(lifecycle.asMviLifecycle())
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    controller.onViewCreated(
      SplashViewImpl(view),
      viewLifecycleOwner.lifecycle.asMviLifecycle()
    )
  }

  interface Dependencies {
    val storeFactory: StoreFactory
    val splashOutput: (SplashController.Output) -> Unit
    val rootRepository: RootRepository
  }
}