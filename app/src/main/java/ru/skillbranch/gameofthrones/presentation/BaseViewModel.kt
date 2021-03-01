package ru.skillbranch.gameofthrones.presentation

import io.reactivex.Observable

interface BaseViewModel<INTENT : BaseIntent, STATE : BaseViewState> {

  fun processIntents(intents: Observable<INTENT>)

  fun states(): Observable<STATE>
}