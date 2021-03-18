package ru.skillbranch.gameofthrones.ui.characters_list

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arkivanov.mvikotlin.core.utils.diff
import com.arkivanov.mvikotlin.core.view.BaseMviView
import com.arkivanov.mvikotlin.core.view.ViewRenderer
import ru.skillbranch.gameofthrones.R
import ru.skillbranch.gameofthrones.getViewById
import ru.skillbranch.gameofthrones.presentation.characters.CharactersListView
import ru.skillbranch.gameofthrones.presentation.characters.CharactersListView.Event
import ru.skillbranch.gameofthrones.presentation.characters.CharactersListView.Model

class CharactersListViewImpl(root: View) :
  BaseMviView<Model, Event>(), CharactersListView {

  private val adapter = CharactersListAdapter { dispatch(Event.ItemClicked(it)) }

  override val renderer: ViewRenderer<Model> =
    diff {
      diff(get = Model::characters, compare = { a, b -> a === b }, set = adapter::setItems)
    }

  init {
    val recyclerView = root.getViewById<RecyclerView>(R.id.recycler_view)
    recyclerView.layoutManager = LinearLayoutManager(root.context)
    recyclerView.adapter = adapter
  }
}