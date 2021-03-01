package ru.skillbranch.gameofthrones.ui.main

import ru.skillbranch.gameofthrones.ui.base.activity.BaseActivityCallback

interface MainActivityCallback: BaseActivityCallback {
  fun navigateToSplash()
  fun navigateToCharactersList()
  fun navigateToCharacter()
}