package ru.skillbranch.gameofthrones.ui.list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.arkivanov.mvikotlin.core.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.lifecycle.asMviLifecycle
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.keepers.instancekeeper.InstanceKeeper
import com.arkivanov.mvikotlin.keepers.instancekeeper.getInstanceKeeper
import ru.skillbranch.gameofthrones.R
import ru.skillbranch.gameofthrones.data.local.cache.CharacterDao
import ru.skillbranch.gameofthrones.data.local.cache.HouseDao
import ru.skillbranch.gameofthrones.presentation.list.ListController
import ru.skillbranch.gameofthrones.presentation.list.ListController.Input
import ru.skillbranch.gameofthrones.presentation.list.ListController.Output
import ru.skillbranch.gameofthrones.presentation.list.ListControllerImpl
import ru.skillbranch.gameofthrones.ui.main.LifecycledConsumer

class CharactersListContainerFragment(
  private val dependencies: Dependencies
) : Fragment(R.layout.list_container), LifecycledConsumer<Input> {


  private lateinit var controller: ListController
  override val input: (Input) -> Unit get() = controller.input

  private fun createController(lifecycle: Lifecycle): ListController {
    return ListControllerImpl(
      object : ListController.Dependencies, Dependencies by dependencies {
        override val instanceKeeper: InstanceKeeper = getInstanceKeeper()
        override val lifecycle: Lifecycle = lifecycle
      }
    )
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    controller = createController(lifecycle.asMviLifecycle())
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    controller.onViewCreated(
      ListViewImpl(view, childFragmentManager),
      SearchViewImpl(view),
      viewLifecycleOwner.lifecycle.asMviLifecycle()
    )
  }

  interface Dependencies {
    val storeFactory: StoreFactory
    val houseDao: HouseDao
    val characterDao: CharacterDao
    val output: (Output) -> Unit
  }
}