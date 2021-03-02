package ru.skillbranch.gameofthrones.presentation.splash

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import ru.skillbranch.gameofthrones.presentation.splash.SplashStore.Intent
import ru.skillbranch.gameofthrones.presentation.splash.SplashStore.State


interface SplashStore : Store<Intent, State, Nothing> {

  sealed class Intent : JvmSerializable {
  }

  data class State(
    val loading: Boolean = false,
    val loaded: Boolean = false,
    val error: String? = null
  ) : JvmSerializable

}