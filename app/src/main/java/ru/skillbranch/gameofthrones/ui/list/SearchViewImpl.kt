package ru.skillbranch.gameofthrones.ui.list

import android.view.View
import com.arkivanov.mvikotlin.core.view.BaseMviView
import ru.skillbranch.gameofthrones.presentation.search.SearchView
import ru.skillbranch.gameofthrones.presentation.search.SearchView.Event
import ru.skillbranch.gameofthrones.presentation.search.SearchView.Model


class SearchViewImpl(view: View) : BaseMviView<Model, Event>(), SearchView {
}