package ru.skillbranch.gameofthrones.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.arkivanov.mvikotlin.core.store.StoreFactory
import ru.skillbranch.gameofthrones.R
import ru.skillbranch.gameofthrones.repositories.RootRepository
import ru.skillbranch.gameofthrones.storeFactoryInstance

class MainActivity : AppCompatActivity() {

  private val fragmentFactory = MainActivityFragmentFactoryImpl()

  override fun onCreate(savedInstanceState: Bundle?) {
    supportFragmentManager.fragmentFactory = fragmentFactory
    super.onCreate(savedInstanceState)

    setContentView(R.layout.content)

    if (savedInstanceState == null) {
      supportFragmentManager
        .beginTransaction()
        .add(R.id.content_container, fragmentFactory.rootFragment())
        .commit()
    }
  }

  override fun onBackPressed() {
    supportFragmentManager
      .fragments
      .forEach {
        if ((it as? RootFragment)?.onBackPressed() == true) {
          return
        }
      }

    super.onBackPressed()
  }

  private inner class MainActivityFragmentFactoryImpl : FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment =
      when (loadFragmentClass(classLoader, className)) {
        RootFragment::class.java -> rootFragment()
        else -> super.instantiate(classLoader, className)
      }

    fun rootFragment(): RootFragment =
      RootFragment(
        object : RootFragment.Dependencies {
          override val storeFactory: StoreFactory get() = storeFactoryInstance
          override val rootRepository: RootRepository = RootRepository
        }
      )
  }
}