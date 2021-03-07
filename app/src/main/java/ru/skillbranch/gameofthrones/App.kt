package ru.skillbranch.gameofthrones

import android.app.Application
import androidx.room.Room
import ru.skillbranch.gameofthrones.data.local.cache.AppDatabase
import ru.skillbranch.gameofthrones.repositories.RootRepository


class App : Application() {

  override fun onCreate() {
    super.onCreate()
    debugLog("onCreate ")

    initDb()
  }

  private fun initDb() {
    val db = Room.databaseBuilder(
      applicationContext,
      AppDatabase::class.java, "game_of_thrones"
    ).build()

    RootRepository.houseDao = db.houseDao()
    RootRepository.characterDao = db.characterDao()
  }

}