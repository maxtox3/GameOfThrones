package ru.skillbranch.gameofthrones.ui.houses_list

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ViewPagerAdapter(manager: FragmentManager) :
  FragmentPagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

  private var data: List<Pair<String, Fragment>> = emptyList()

  override fun getItem(position: Int): Fragment {
    return data[position].second
  }

  override fun getCount(): Int {
    return data.size
  }

  fun setData(data: Map<String, Fragment>) {
    val newItems = data.map { it.key to it.value }
    this.data = newItems
    notifyDataSetChanged()
  }

  override fun getPageTitle(position: Int): CharSequence {
    return data[position].first
  }
}