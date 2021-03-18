package ru.skillbranch.gameofthrones.ui.houses_list

import android.app.Activity
import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.arkivanov.mvikotlin.core.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.lifecycle.asMviLifecycle
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.keepers.instancekeeper.InstanceKeeper
import com.arkivanov.mvikotlin.keepers.instancekeeper.getInstanceKeeper
import ru.skillbranch.gameofthrones.R
import ru.skillbranch.gameofthrones.data.local.cache.CharacterDao
import ru.skillbranch.gameofthrones.data.local.cache.HouseDao
import ru.skillbranch.gameofthrones.debugLog
import ru.skillbranch.gameofthrones.presentation.characters.CharactersListController
import ru.skillbranch.gameofthrones.presentation.houses.HousesListController
import ru.skillbranch.gameofthrones.presentation.houses.HousesListController.Input
import ru.skillbranch.gameofthrones.presentation.houses.HousesListControllerImpl
import ru.skillbranch.gameofthrones.ui.main.LifecycledConsumer


class HousesListFragment(
  private val dependencies: Dependencies
) : Fragment(R.layout.list_container), LifecycledConsumer<Input> {

  private lateinit var controller: HousesListController
  override val input: (Input) -> Unit get() = controller.input

  private fun createController(lifecycle: Lifecycle): HousesListController {
    return HousesListControllerImpl(
      object : HousesListController.Dependencies, Dependencies by dependencies {
        override val instanceKeeper: InstanceKeeper = getInstanceKeeper()
        override val lifecycle: Lifecycle = lifecycle
      }
    )
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setHasOptionsMenu(true)

    controller = createController(lifecycle.asMviLifecycle())
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    controller.onViewCreated(
      HousesListViewImpl(
        root = view,
        dependencies = object : HousesListViewImpl.Dependencies, Dependencies by dependencies {},
        fragmentManager = childFragmentManager
      ),
      SearchViewImpl(view),
      viewLifecycleOwner.lifecycle.asMviLifecycle()
    )
  }

  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    inflater.inflate(R.menu.menu, menu)
    val searchItem: MenuItem = menu.findItem(R.id.action_search)
    val searchManager = context?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
    val searchView = searchItem.actionView as SearchView
    searchView.setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
    val queryTextListener = object : SearchView.OnQueryTextListener {
      override fun onQueryTextChange(newText: String?): Boolean {
        debugLog(newText)
        return true
      }

      override fun onQueryTextSubmit(query: String?): Boolean {
        debugLog(query)
        return true
      }
    }
    searchView.setOnQueryTextListener(queryTextListener)
    super.onCreateOptionsMenu(menu, inflater)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      R.id.action_search -> return false
      else -> { }
    }
    return super.onOptionsItemSelected(item)
  }

  interface Dependencies {
    val storeFactory: StoreFactory
    val houseDao: HouseDao
    val characterDao: CharacterDao
    val charactersOutput: (CharactersListController.Output) -> Unit
    val housesOutput: (HousesListController.Output) -> Unit
  }
}