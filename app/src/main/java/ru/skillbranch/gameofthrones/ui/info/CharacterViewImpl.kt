package ru.skillbranch.gameofthrones.ui.info

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.arkivanov.mvikotlin.core.utils.diff
import com.arkivanov.mvikotlin.core.view.BaseMviView
import com.arkivanov.mvikotlin.core.view.ViewRenderer
import ru.skillbranch.gameofthrones.R
import ru.skillbranch.gameofthrones.getViewById
import ru.skillbranch.gameofthrones.presentation.info.CharacterView
import ru.skillbranch.gameofthrones.presentation.info.CharacterView.Event
import ru.skillbranch.gameofthrones.presentation.info.CharacterView.Model

class CharacterViewImpl(root: View) : BaseMviView<Model, Event>(), CharacterView {

  private val image = root.getViewById<ImageView>(R.id.character_imv)
  private val toolbar = root.getViewById<Toolbar>(R.id.toolbar)
  private val words = root.getViewById<TextView>(R.id.words_tv)
  private val born = root.getViewById<TextView>(R.id.born_tv)
  private val titles = root.getViewById<TextView>(R.id.titles_tv)
  private val aliases = root.getViewById<TextView>(R.id.aliases_tv)

  override val renderer: ViewRenderer<Model> =
    diff {
      diff(Model::character) {
        it?.let { character ->
          image.setImageResource(R.drawable.martel_coast_of_arms)
          toolbar.title = character.name
          born.text = character.born
          titles.text = character.titles.joinToString()
          aliases.text = character.aliases.joinToString()
          words.text = character.words
        }
      }
    }
}