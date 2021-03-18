package ru.skillbranch.gameofthrones.ui.characters_list

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
import ru.skillbranch.gameofthrones.presentation.characters.CharactersListController
import ru.skillbranch.gameofthrones.presentation.characters.CharactersListControllerImpl

class CharactersListFragment(
  private val dependencies: Dependencies
) : Fragment(R.layout.list) {

  private lateinit var controller: CharactersListController
  private val args: Arguments by lazy {
    requireArguments().apply {
      classLoader = Arguments::class.java.classLoader
    }.getParcelable<Arguments>(KEY_ARGUMENTS) as Arguments
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    controller = CharactersListControllerImpl(
      object : CharactersListController.Dependencies, Dependencies by dependencies {
        override val instanceKeeper: InstanceKeeper = getInstanceKeeper()
        override val houseId: String = args.id
        override val lifecycle: Lifecycle = this@CharactersListFragment.lifecycle.asMviLifecycle()
      }
    )
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    controller.onViewCreated(
      CharactersListViewImpl(view),
      viewLifecycleOwner.lifecycle.asMviLifecycle()
    )
  }

  fun setArguments(houseId: String): CharactersListFragment {
    arguments = bundleOf(KEY_ARGUMENTS to Arguments(id = houseId))
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
    val charactersOutput: (CharactersListController.Output) -> Unit
  }
}