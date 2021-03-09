package ru.skillbranch.gameofthrones.presentation.splash

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import ru.skillbranch.gameofthrones.presentation.splash.SplashStore.*


interface SplashStore : Store<Intent, State, Label> {

  sealed class Intent : JvmSerializable {
  }

  data class State(
    val loading: Boolean = false,
    val loaded: Boolean = false,
    val error: String? = null
  ) : JvmSerializable

  sealed class Label : JvmSerializable {
    object DataLoaded : Label()
  }

}