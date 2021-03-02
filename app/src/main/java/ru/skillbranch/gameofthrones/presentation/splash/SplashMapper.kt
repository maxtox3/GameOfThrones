package ru.skillbranch.gameofthrones.presentation.splash

import ru.skillbranch.gameofthrones.presentation.splash.SplashStore.State
import ru.skillbranch.gameofthrones.presentation.splash.SplashView.Model

val splashStateToSplashModel: State.() -> Model? = {
  Model(
    loaded = loaded,
    error = error,
    loading = loading
  )
}