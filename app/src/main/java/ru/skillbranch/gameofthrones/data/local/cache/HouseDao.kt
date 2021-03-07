package ru.skillbranch.gameofthrones.data.local.cache

import androidx.room.*
import ru.skillbranch.gameofthrones.data.local.entities.House

@Dao
interface HouseDao {

  @Query("SELECT * FROM house")
  suspend fun all(): List<House?>?

  @Query("SELECT * FROM house WHERE id IN (:houseIds)")
  suspend fun getAllByIds(vararg houseIds: String): List<House?>?

  @Query("SELECT * FROM house WHERE name LIKE :name LIMIT 1")
  suspend fun getByName(name: String): House?

  @Query("SELECT * FROM house WHERE id LIKE :id LIMIT 1")
  suspend fun getById(id: String): House?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAll(vararg houses: House)

  @Delete
  suspend fun delete(house: House)

  @Query("DELETE FROM house")
  fun nukeTable()

}