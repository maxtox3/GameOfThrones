package ru.skillbranch.gameofthrones.ui.houses_list

import android.view.View
import android.widget.EditText
import com.arkivanov.mvikotlin.core.view.BaseMviView
import ru.skillbranch.gameofthrones.presentation.search.SearchView
import ru.skillbranch.gameofthrones.presentation.search.SearchView.Event
import ru.skillbranch.gameofthrones.presentation.search.SearchView.Model


class SearchViewImpl(root: View) : BaseMviView<Model, Event>(), SearchView {

  init {
//    root.findViewById<EditText>()
  }
}