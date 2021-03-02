package ru.skillbranch.gameofthrones.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.arkivanov.mvikotlin.core.store.StoreFactory
import ru.skillbranch.gameofthrones.R
import ru.skillbranch.gameofthrones.presentation.splash.SplashController
import ru.skillbranch.gameofthrones.repositories.RootRepository
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
        .add(R.id.content_container, fragmentFactory.splashFragment())
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
    openList()
//    when (output) {
//      is SplashController.Loaded -> openList(itemId = output.id)
//      is SplashController.Error -> openList(itemId = output.id)
//    }.let {}
  }

  private fun openList() {
    childFragmentManager
      .beginTransaction()
//      .setCustomAnimations(
//        R.anim.slide_fade_in_bottom,
//        R.anim.scale_fade_out,
//        R.anim.scale_fade_in,
//        R.anim.slide_fade_out_bottom
//      )
      .replace(R.id.content_container, fragmentFactory.splashFragment())
      .addToBackStack(null)
      .commit()
  }

  interface Dependencies {
    val storeFactory: StoreFactory
    val rootRepository: RootRepository
//    val database: Database
  }

  private inner class FragmentFactoryImpl : FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment =
      when (loadFragmentClass(classLoader, className)) {
        SplashFragment::class.java -> splashFragment()
//        CharactersListFragment::class.java -> characterListFragment()
//        CharacterFragment::class.java -> characterFragment()
        else -> super.instantiate(classLoader, className)
      }

    fun splashFragment(): SplashFragment =
      SplashFragment(
        object : SplashFragment.Dependencies, Dependencies by dependencies {
          override val splashOutput: (SplashController.Output) -> Unit = ::handleSplashOutput
        }
      )

//    fun characterFragment(): CharacterFragment =
//      CharacterFragment(
//        object : CharacterFragment.Dependencies, Dependencies by dependencies {
//        }
//      )
//
//    fun characterListFragment(): CharactersListFragment =
//      CharactersListFragment(
//        object : CharactersListFragment.Dependencies, Dependencies by dependencies {
//        }
//      )
  }
}