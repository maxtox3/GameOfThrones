package ru.skillbranch.gameofthrones.ui.list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.skillbranch.gameofthrones.R
import ru.skillbranch.gameofthrones.data.local.entities.CharacterItem
import ru.skillbranch.gameofthrones.getViewById

class CharactersListFragment(
  private val dependencies: Dependencies
) : Fragment(R.layout.list) {

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    val recycler = view.getViewById<RecyclerView>(R.id.recycler_view)
    val adapter = CharactersListAdapter(onItemClick = dependencies.onItemClick)

    recycler.layoutManager = LinearLayoutManager(view.context)
    recycler.adapter = adapter
    adapter.setItems(dependencies.data)
  }

  interface Dependencies {
    val data: List<CharacterItem>
    val onItemClick: (CharacterItem) -> Unit
  }
}