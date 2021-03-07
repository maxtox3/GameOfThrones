package ru.skillbranch.gameofthrones.data.local.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.skillbranch.gameofthrones.data.local.entities.House
import ru.skillbranch.gameofthrones.data.local.entities.Character


@Database(entities = [Character::class, House::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
  abstract fun houseDao(): HouseDao
  abstract fun characterDao(): CharacterDao
}