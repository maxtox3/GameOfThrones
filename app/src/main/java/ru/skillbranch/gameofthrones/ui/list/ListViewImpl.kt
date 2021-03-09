package ru.skillbranch.gameofthrones.ui.list

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.app.Activity
import android.graphics.drawable.ColorDrawable
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.ViewPager
import com.arkivanov.mvikotlin.core.utils.diff
import com.arkivanov.mvikotlin.core.view.BaseMviView
import com.arkivanov.mvikotlin.core.view.ViewRenderer
import com.google.android.material.tabs.TabLayout
import ru.skillbranch.gameofthrones.R
import ru.skillbranch.gameofthrones.colorIdByHouseName
import ru.skillbranch.gameofthrones.data.local.entities.CharacterItem
import ru.skillbranch.gameofthrones.getViewById
import ru.skillbranch.gameofthrones.presentation.list.ListView
import ru.skillbranch.gameofthrones.presentation.list.ListView.Event
import ru.skillbranch.gameofthrones.presentation.list.ListView.Model


class ListViewImpl(
  private val root: View,
  fragmentManager: FragmentManager
) : BaseMviView<Model, Event>(), ListView {

  private val pagerAdapter = ViewPagerAdapter(fragmentManager)

  private val tabLayout = root.getViewById<TabLayout>(R.id.tabLayout)
  private val toolbar = root.getViewById<Toolbar>(R.id.toolbar)
  private val viewPager = root.getViewById<ViewPager>(R.id.pager)

  override val renderer: ViewRenderer<Model> =
    diff {
      diff(Model::tabs) { if (it.isNotEmpty()) updateTabs(it) }

      diff(
        get = Model::housesWithCharacters,
        compare = { a, b -> a === b },
        set = { updateFragments(it) }
      )
    }

  init {
    viewPager.adapter = pagerAdapter
  }

  private fun updateTabs(tabs: List<String>) {
    tabLayout.removeAllTabs()
    if (tabs.isNotEmpty()) {
      tabs.forEach { tabLayout.addTab(tabLayout.newTab().setText(it)) }
      tabLayout.addOnTabSelectedListener(onTabSelectedListener(tabs))
      val colorId = root.context.colorIdByHouseName(tabs[tabLayout.selectedTabPosition])
      toolbar.setBackgroundColor(colorId)
      tabLayout.setBackgroundColor(colorId)
    }

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

  private fun updateFragments(data: Map<String, List<CharacterItem>>) {
    val fragmentsToSet = data.map { (key, listOfCharacters) ->
      key to CharactersListFragment(
        dependencies = object : CharactersListFragment.Dependencies {
          override val data: List<CharacterItem> = listOfCharacters
          override val onItemClick: (CharacterItem) -> Unit =
            { dispatch(Event.ItemClicked(it)) }
        })
    }.toMap()
    pagerAdapter.setData(fragmentsToSet)
  }
}
