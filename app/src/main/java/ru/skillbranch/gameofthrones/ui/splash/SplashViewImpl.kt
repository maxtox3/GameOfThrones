package ru.skillbranch.gameofthrones.ui.splash

import android.view.View
import android.widget.ImageView
import com.arkivanov.mvikotlin.core.utils.diff
import com.arkivanov.mvikotlin.core.view.BaseMviView
import com.arkivanov.mvikotlin.core.view.ViewRenderer
import ru.skillbranch.gameofthrones.R
import ru.skillbranch.gameofthrones.animateColorFilter
import ru.skillbranch.gameofthrones.getViewById
import ru.skillbranch.gameofthrones.presentation.splash.SplashView
import ru.skillbranch.gameofthrones.presentation.splash.SplashView.Model


class SplashViewImpl(root: View) : BaseMviView<Model, Nothing>(), SplashView {

  private val image = root.getViewById<ImageView>(R.id.splash_image)

  override val renderer: ViewRenderer<Model> =
    diff {
      diff(Model::mainLoading) {
        image.animateColorFilter()
      }
    }
}