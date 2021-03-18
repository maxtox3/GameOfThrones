package ru.skillbranch.gameofthrones.presentation.houses

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import ru.skillbranch.gameofthrones.data.local.entities.House
import ru.skillbranch.gameofthrones.presentation.houses.HousesListStore.*


interface HousesListStore : Store<Intent, State, Label> {

  sealed class Intent : JvmSerializable {
  }

  data class State(
    val loading: Boolean = false,
    val data: List<House> = emptyList(),
    val error: String? = null
  ) : JvmSerializable

  sealed class Label {

  }

}