package ru.skillbranch.gameofthrones.presentation.splash

import com.arkivanov.mvikotlin.core.binder.BinderLifecycleMode
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.lifecycle.doOnDestroy
import com.arkivanov.mvikotlin.extensions.reaktive.bind
import com.arkivanov.mvikotlin.extensions.reaktive.states
import com.badoo.reaktive.observable.mapNotNull
import ru.skillbranch.gameofthrones.presentation.splash.SplashController.Dependencies

class SplashControllerImpl(
  private val dependencies: Dependencies
) : SplashController {

  private val store =
    dependencies.instanceKeeper.getStore {
      SplashStoreFactory(
        storeFactory = dependencies.storeFactory,
        rootRepository = dependencies.rootRepository
      ).create()
    }

  init {
    dependencies.lifecycle.doOnDestroy(store::dispose)
  }

  override fun onViewCreated(splashView: SplashView, viewLifecycle: Lifecycle) {
    bind(viewLifecycle, BinderLifecycleMode.START_STOP) {
      store.states.mapNotNull(splashStateToSplashModel) bindTo splashView
      // bindTo dependencies.output
    }
  }
}