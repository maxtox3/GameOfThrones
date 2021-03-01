package ru.skillbranch.gameofthrones

import android.util.Log

fun Any.debugLog(text: String) {
  Log.d(this.javaClass.name, text)
}