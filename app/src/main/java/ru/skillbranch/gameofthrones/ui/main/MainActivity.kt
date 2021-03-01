package ru.skillbranch.gameofthrones.ui.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import ru.skillbranch.gameofthrones.Screen
import ru.skillbranch.gameofthrones.R
import ru.skillbranch.gameofthrones.ui.base.activity.BaseActivityFragmentContainer
import ru.skillbranch.gameofthrones.ui.info.CharacterFragment
import ru.skillbranch.gameofthrones.ui.list.CharactersListFragment
import ru.skillbranch.gameofthrones.ui.splash.SplashFragment

class MainActivity : BaseActivityFragmentContainer(), MainActivityCallback {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    if (savedInstanceState == null) {
      navigateToSplash()
    }
  }

  override fun navigateToSplash() {
    navigateToFragment(Screen.SplashScreen, null, false)
  }

  override fun navigateToCharactersList() {
    navigateToFragment(Screen.CharactersListScreen, null, false)
  }

  override fun navigateToCharacter() {
    navigateToFragment(Screen.CharacterScreen, null, false)
  }

  override fun createFragment(tag: String, args: Bundle?): Fragment {
    when (tag) {
      Screen.SplashScreen -> return SplashFragment()
      Screen.CharacterScreen -> return CharacterFragment()
      Screen.CharactersListScreen -> return CharactersListFragment()
      else -> Log.i("createFragment: ", "you must add your fragment")
    }
    return Fragment()
  }

  override fun getContentViewResourceId(): Int {
    return R.layout.activity_main
  }

  override fun setContainerId() {
    setContainerId(R.id.fragment_container)
  }

  override fun setupWidgets() {
//    val nav = findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
//    nav.setOnNavigationItemSelectedListener(
//      { item ->
//        when (item.itemId) {
//          R.id.map_button -> navigateToMap()
////                        R.id.top_button -> navigateToTop()
////                        R.id.favorites_button -> navigateToFavorites()
////                        R.id.profile_button -> navigateToProfile()
//        }
//        true
//      })
  }
}