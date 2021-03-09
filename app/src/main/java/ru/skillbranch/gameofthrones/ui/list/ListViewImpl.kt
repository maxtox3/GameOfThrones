package ru.skillbranch.gameofthrones.ui.list

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.ViewPager
import com.arkivanov.mvikotlin.core.utils.diff
import com.arkivanov.mvikotlin.core.view.BaseMviView
import com.arkivanov.mvikotlin.core.view.ViewRenderer
import com.google.android.material.tabs.TabLayout
import ru.skillbranch.gameofthrones.R
import ru.skillbranch.gameofthrones.data.local.entities.CharacterItem
import ru.skillbranch.gameofthrones.getViewById
import ru.skillbranch.gameofthrones.presentation.list.ListView
import ru.skillbranch.gameofthrones.presentation.list.ListView.Event
import ru.skillbranch.gameofthrones.presentation.list.ListView.Model

class ListViewImpl(
  root: View,
  fragmentManager: FragmentManager
) : BaseMviView<Model, Event>(), ListView {

  private val adapter = ViewPagerAdapter(fragmentManager)

  private val tabLayout = root.getViewById<TabLayout>(R.id.tabLayout)
  private val fragments = mutableMapOf<String, Fragment>()
  private val viewPager = root.getViewById<ViewPager>(R.id.pager)


  override val renderer: ViewRenderer<Model> =
    diff {
      diff(Model::tabs) { tabs ->
        tabLayout.removeAllTabs()
        tabs.forEach {
          tabLayout.addTab(tabLayout.newTab().setText(it))
          tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab) {
              viewPager.currentItem = tabs.indexOf(tab.text)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}

          })
        }
      }

      diff(
        get = Model::housesWithCharacters,
        compare = { a, b -> a === b },
        set = { updateFragments(it) }
      )
    }

  private fun updateFragments(data: Map<String, List<CharacterItem>>) {
    val fragmentsToSet = data.map { (key, listOfCharacters) ->
      key to CharactersListFragment(dependencies = object : CharactersListFragment.Dependencies {
        override val data: List<CharacterItem> = listOfCharacters
        override val onItemClick: (CharacterItem) -> Unit = { dispatch(Event.ItemClicked(it)) }
      })
    }.toMap()
    adapter.setData(fragmentsToSet)
  }

  init {
    viewPager.adapter = adapter
  }
}
