package ru.skillbranch.gameofthrones

import android.app.Application
import androidx.room.Room
import ru.skillbranch.gameofthrones.data.local.cache.AppDatabase
import ru.skillbranch.gameofthrones.data.local.cache.CharacterDao
import ru.skillbranch.gameofthrones.data.local.cache.HouseDao
import ru.skillbranch.gameofthrones.repositories.RootRepository


class App : Application() {

  lateinit var houseDao: HouseDao
  lateinit var characterDao: CharacterDao

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

    characterDao = db.characterDao()
    houseDao = db.houseDao()
    RootRepository.houseDao = houseDao
    RootRepository.characterDao = characterDao
  }

}