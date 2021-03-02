package ru.skillbranch.gameofthrones.ui.main

import androidx.lifecycle.LifecycleOwner

interface LifecycledConsumer<in T> : LifecycleOwner {

    val input: (T) -> Unit
}
