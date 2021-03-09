package ru.skillbranch.gameofthrones.presentation.splash

import ru.skillbranch.gameofthrones.presentation.splash.SplashController.Output
import ru.skillbranch.gameofthrones.presentation.splash.SplashStore.Label
import ru.skillbranch.gameofthrones.presentation.splash.SplashStore.State
import ru.skillbranch.gameofthrones.presentation.splash.SplashView.Model

val splashStateToSplashModel: State.() -> Model? = {
  Model(
    loaded = loaded,
    error = error,
    mainLoading = loading
  )
}

val splashLabelToOutput: Label.() -> Output? = {
  when (this) {
    Label.DataLoaded -> Output.DataLoaded
  }
}