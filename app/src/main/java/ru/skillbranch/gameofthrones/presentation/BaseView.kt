package ru.skillbranch.gameofthrones.presentation

import io.reactivex.Observable

interface BaseView<INTENT: BaseIntent, in STATE: BaseViewState> {
  fun intents(): Observable<INTENT>

  fun render(state: STATE)
}