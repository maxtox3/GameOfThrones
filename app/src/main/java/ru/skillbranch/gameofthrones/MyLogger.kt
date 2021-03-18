package ru.skillbranch.gameofthrones

import android.util.Log
import com.arkivanov.mvikotlin.logging.logger.Logger

object MyLogger : Logger {
  override fun log(text: String) {
    Log.v("[MVI]", text)
  }
}