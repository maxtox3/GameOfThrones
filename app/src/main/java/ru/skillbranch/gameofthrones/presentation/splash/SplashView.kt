package ru.skillbranch.gameofthrones.presentation.splash

import com.arkivanov.mvikotlin.core.view.MviView
import ru.skillbranch.gameofthrones.presentation.splash.SplashView.Model

interface SplashView : MviView<Model, Nothing> {

  data class Model(
    val loaded: Boolean = false,
    val error: String? = null,
    val loading: Boolean = false
  )

}