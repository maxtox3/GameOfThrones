package ru.skillbranch.gameofthrones.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.arkivanov.mvikotlin.core.store.StoreFactory
import ru.skillbranch.gameofthrones.R
import ru.skillbranch.gameofthrones.data.local.cache.CharacterDao
import ru.skillbranch.gameofthrones.data.local.cache.HouseDao
import ru.skillbranch.gameofthrones.presentation.info.CharacterController
import ru.skillbranch.gameofthrones.presentation.list.ListController
import ru.skillbranch.gameofthrones.presentation.splash.SplashController
import ru.skillbranch.gameofthrones.repositories.RootRepository
import ru.skillbranch.gameofthrones.ui.info.CharacterFragment
import ru.skillbranch.gameofthrones.ui.list.CharactersListContainerFragment
import ru.skillbranch.gameofthrones.ui.splash.SplashFragment

class RootFragment(
  private val dependencies: Dependencies
) : Fragment(R.layout.content) {

  private val fragmentFactory = FragmentFactoryImpl()

  override fun onCreate(savedInstanceState: Bundle?) {
    childFragmentManager.fragmentFactory = fragmentFactory

    super.onCreate(savedInstanceState)

    if (savedInstanceState == null) {
      childFragmentManager
        .beginTransaction()
        .add(R.id.content_container, fragmentFactory.characterListFragment())
        .commit()
    }
  }

  fun onBackPressed(): Boolean =
    if (childFragmentManager.backStackEntryCount > 0) {
      childFragmentManager.popBackStack()
      true
    } else {
      false
    }

  private fun handleSplashOutput(output: SplashController.Output) {
    when (output) {
      is SplashController.Output.DataLoaded -> openList()
    }.let {}
  }

  private fun handleListOutput(output: ListController.Output) {
    when (output) {
      is ListController.Output.OpenCharacterInfo -> openCharacterInfo(output.id)
    }.let {}
  }

  private fun handleCharacterOutput(output: CharacterController.Output) {
    when (output) {
      CharacterController.Output.DataLoaded -> { }
    }.let {}
  }

  private fun openCharacterInfo(id: String) {
    childFragmentManager
      .beginTransaction()
      .replace(R.id.content_container, fragmentFactory.characterFragment().setArguments(characterId = id))
      .addToBackStack(null)
      .commit()
  }

  private fun openList() {
    childFragmentManager
      .beginTransaction()
      .replace(R.id.content_container, fragmentFactory.splashFragment())
      .addToBackStack(null)
      .commit()
  }

  private inner class FragmentFactoryImpl : FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment =
      when (loadFragmentClass(classLoader, className)) {
        SplashFragment::class.java -> splashFragment()
        CharactersListContainerFragment::class.java -> characterListFragment()
        CharacterFragment::class.java -> characterFragment()
        else -> super.instantiate(classLoader, className)
      }

    fun splashFragment(): SplashFragment =
      SplashFragment(
        object : SplashFragment.Dependencies, Dependencies by dependencies {
          override val splashOutput: (SplashController.Output) -> Unit = ::handleSplashOutput
        }
      )

    fun characterFragment(): CharacterFragment =
      CharacterFragment(
        object : CharacterFragment.Dependencies, Dependencies by dependencies {
          override val output: (CharacterController.Output) -> Unit = ::handleCharacterOutput
        }
      )

    fun characterListFragment(): CharactersListContainerFragment =
      CharactersListContainerFragment(
        object : CharactersListContainerFragment.Dependencies, Dependencies by dependencies {
          override val output: (ListController.Output) -> Unit = ::handleListOutput
        }
      )
  }

  interface Dependencies {
    val storeFactory: StoreFactory
    val rootRepository: RootRepository
    val houseDao: HouseDao
    val characterDao: CharacterDao
//    val database: Database
  }
}