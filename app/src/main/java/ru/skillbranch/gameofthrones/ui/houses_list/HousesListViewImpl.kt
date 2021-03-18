package ru.skillbranch.gameofthrones.ui.houses_list

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.app.Activity
import android.graphics.drawable.ColorDrawable
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.ViewPager
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.diff
import com.arkivanov.mvikotlin.core.view.BaseMviView
import com.arkivanov.mvikotlin.core.view.ViewRenderer
import com.google.android.material.tabs.TabLayout
import ru.skillbranch.gameofthrones.R
import ru.skillbranch.gameofthrones.colorIdByHouseName
import ru.skillbranch.gameofthrones.data.local.cache.CharacterDao
import ru.skillbranch.gameofthrones.data.local.cache.HouseDao
import ru.skillbranch.gameofthrones.data.local.entities.House
import ru.skillbranch.gameofthrones.getViewById
import ru.skillbranch.gameofthrones.presentation.characters.CharactersListController
import ru.skillbranch.gameofthrones.presentation.houses.HousesListView
import ru.skillbranch.gameofthrones.presentation.houses.HousesListView.Event
import ru.skillbranch.gameofthrones.presentation.houses.HousesListView.Model
import ru.skillbranch.gameofthrones.shortenHouseName
import ru.skillbranch.gameofthrones.ui.characters_list.CharactersListFragment


class HousesListViewImpl(
  private val root: View,
  private val dependencies: Dependencies,
  fragmentManager: FragmentManager
) : BaseMviView<Model, Event>(), HousesListView {

  private val pagerAdapter = ViewPagerAdapter(fragmentManager)

  private val tabLayout = root.getViewById<TabLayout>(R.id.tabLayout)
  private val toolbar = root.getViewById<Toolbar>(R.id.toolbar)
  private val viewPager = root.getViewById<ViewPager>(R.id.pager)

  override val renderer: ViewRenderer<Model> =
    diff {
      diff(Model::houses) { list ->
        updateTabs(list)
        updateFragments(list)
      }
    }

  init {
    viewPager.adapter = pagerAdapter
  }

  private fun updateTabs(houses: List<House>) {
    val tabs = houses.map { shortenHouseName(it.name) }
    tabLayout.removeAllTabs()
    if (tabs.isNotEmpty()) {
      tabs.forEach { tabLayout.addTab(tabLayout.newTab().setText(it)) }
      tabLayout.addOnTabSelectedListener(onTabSelectedListener(tabs))
      val colorId = root.context.colorIdByHouseName(tabs[tabLayout.selectedTabPosition])
      toolbar.setBackgroundColor(colorId)
      tabLayout.setBackgroundColor(colorId)
    }
  }

  private fun updateFragments(houses: List<House>) {
    val fragmentsToSet = houses.map {
      it.name to CharactersListFragment(
        dependencies = object : CharactersListFragment.Dependencies, Dependencies by dependencies {}
      ).setArguments(it.id)
    }.toMap()
    pagerAdapter.setData(fragmentsToSet)
  }

  private fun onTabSelectedListener(tabs: List<String>) =
    object : TabLayout.OnTabSelectedListener {
      override fun onTabSelected(tab: TabLayout.Tab) {
        val colorFrom = (toolbar.background as? ColorDrawable)?.color
        val colorTo: Int = root.context.colorIdByHouseName(tabs[tab.position])

        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
        colorAnimation.addUpdateListener { animator ->
          val color = animator.animatedValue as Int
          toolbar.setBackgroundColor(color)
          tabLayout.setBackgroundColor(color)
          (root.context as Activity).window.statusBarColor = color
        }
        colorAnimation.start()
        viewPager.currentItem = tab.position
      }

      override fun onTabUnselected(tab: TabLayout.Tab?) {}
      override fun onTabReselected(tab: TabLayout.Tab?) {}
    }

  interface Dependencies {
    val storeFactory: StoreFactory
    val houseDao: HouseDao
    val characterDao: CharacterDao
    val charactersOutput: (CharactersListController.Output) -> Unit
  }
}
