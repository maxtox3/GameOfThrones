package ru.skillbranch.gameofthrones

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.animation.Animation
import android.widget.EditText
import android.widget.ImageView
import androidx.annotation.IdRes
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.arkivanov.mvikotlin.timetravel.store.TimeTravelStoreFactory
import kotlin.math.roundToInt

fun Any.debugLog(text: String?) {
  println("${this.javaClass.name}: $text")
}

val storeFactoryInstance: StoreFactory =
  if (BuildConfig.DEBUG) {
    LoggingStoreFactory(delegate = TimeTravelStoreFactory(fallback = DefaultStoreFactory))
  } else {
    DefaultStoreFactory
  }

val Context.app: App get() = applicationContext as App

fun <T : Any> T?.requireNotNull(): T = requireNotNull(this)

fun <T : View> View.getViewById(@IdRes id: Int): T = findViewById<T>(id).requireNotNull()

fun EditText.setTextCompat(text: CharSequence, textWatcher: TextWatcher? = null) {
  val savedSelectionStart = selectionStart
  val savedSelectionEnd = selectionEnd
  textWatcher?.also(::removeTextChangedListener)
  setText(text)
  textWatcher?.also(::addTextChangedListener)
  if (savedSelectionEnd <= text.length) {
    setSelection(savedSelectionStart, savedSelectionEnd)
  } else {
    setSelection(text.length)
  }
}

open class SimpleTextWatcher : TextWatcher {
  override fun afterTextChanged(s: Editable) {
    // no-op
  }

  override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
    // no-op
  }

  override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
    // no-op
  }
}

inline fun Lifecycle.doOnDestroy(crossinline block: () -> Unit) {
  addObserver(
    object : LifecycleObserver {
//      override fun onDestroy(owner: LifecycleOwner) {
//        block()
//      }
    }
  )
}


fun ImageView.animateColorFilter() {
  val red: Int = Color.RED
  val colorAnim = ObjectAnimator.ofFloat(0.2f, 0.4f)
  colorAnim.addUpdateListener { animation ->
    val mul = animation.animatedValue as Float
    val alphaRed = adjustAlpha(red, mul)
    setColorFilter(alphaRed, PorterDuff.Mode.SRC_ATOP)
    if (mul.toDouble() == 0.0) {
      colorFilter = null
    }
  }
  colorAnim.duration = 1500
  colorAnim.repeatMode = ValueAnimator.REVERSE
  colorAnim.repeatCount = Animation.INFINITE
  colorAnim.start()
}

private fun adjustAlpha(color: Int, factor: Float): Int {
  val alpha = (Color.alpha(color) * factor).roundToInt()
  val red = Color.red(color)
  val green = Color.green(color)
  val blue = Color.blue(color)
  return Color.argb(alpha, red, green, blue)
}

fun parseId(url: String): String {
  return url.substringAfterLast("/")
}

fun avatarByHouseName(houseName: String): Int = when (houseName) {
  "House Stark of Winterfell" -> R.drawable.stark_icon
  "House Lannister of Casterly Rock" -> R.drawable.lannister_icon
  "House Targaryen of King's Landing" -> R.drawable.targaryen_icon
  "House Greyjoy of Pyke" -> R.drawable.greyjoy_icon
  "House Tyrell of Highgarden" -> R.drawable.tyrel_icon
  "House Baratheon of Dragonstone" -> R.drawable.baratheon_icon
  "House Nymeros Martell of Sunspear" -> R.drawable.martel_icon
  else -> throw NotImplementedError("not implemented avatarByHouseName")
}

fun shortenHouseName(fullHouseName: String): String = when (fullHouseName) {
  "House Stark of Winterfell" -> "Stark"
    "House Lannister of Casterly Rock" -> "Lannister"
    "House Targaryen of King's Landing" -> "Targaryen"
  "House Greyjoy of Pyke" -> "Greyjoy"
    "House Tyrell of Highgarden" -> "Tyrell"
    "House Baratheon of Dragonstone" -> "Baratheon"
  "House Nymeros Martell of Sunspear" -> "Martell"
  else -> throw NotImplementedError("not implemented shortenHouseName")
}