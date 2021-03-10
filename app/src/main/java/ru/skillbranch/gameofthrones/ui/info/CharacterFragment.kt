package ru.skillbranch.gameofthrones.ui.info

import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.arkivanov.mvikotlin.core.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.lifecycle.asMviLifecycle
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.keepers.instancekeeper.InstanceKeeper
import com.arkivanov.mvikotlin.keepers.instancekeeper.getInstanceKeeper
import kotlinx.android.parcel.Parcelize
import ru.skillbranch.gameofthrones.R
import ru.skillbranch.gameofthrones.data.local.cache.CharacterDao
import ru.skillbranch.gameofthrones.data.local.cache.HouseDao
import ru.skillbranch.gameofthrones.presentation.info.CharacterController
import ru.skillbranch.gameofthrones.presentation.info.CharacterControllerImpl

class CharacterFragment(
  private val dependencies: Dependencies
) : Fragment(R.layout.info) {

  private lateinit var controller: CharacterController
  private val args: Arguments by lazy {
    requireArguments().apply {
      classLoader = Arguments::class.java.classLoader
    }.getParcelable<Arguments>(KEY_ARGUMENTS) as Arguments
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    controller = CharacterControllerImpl(
      object : CharacterController.Dependencies, Dependencies by dependencies {
        override val instanceKeeper: InstanceKeeper = getInstanceKeeper()
        override val lifecycle: Lifecycle = this@CharacterFragment.lifecycle.asMviLifecycle()
        override val characterId: String = args.id
      }
    )
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    controller.onViewCreated(
      CharacterViewImpl(view),
      viewLifecycleOwner.lifecycle.asMviLifecycle()
    )
  }

  fun setArguments(characterId: String): CharacterFragment {
    arguments = bundleOf(KEY_ARGUMENTS to Arguments(id = characterId))
    return this
  }

  companion object {
    private const val KEY_ARGUMENTS = "ARGUMENTS"
  }

  @Parcelize
  private class Arguments(val id: String) : Parcelable

  interface Dependencies {
    val storeFactory: StoreFactory
    val houseDao: HouseDao
    val characterDao: CharacterDao
    val output: (CharacterController.Output) -> Unit
  }
}