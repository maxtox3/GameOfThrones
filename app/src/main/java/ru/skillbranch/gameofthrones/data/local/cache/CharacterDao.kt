package ru.skillbranch.gameofthrones.data.local.cache

import androidx.room.*
import ru.skillbranch.gameofthrones.data.local.entities.Character
import ru.skillbranch.gameofthrones.data.local.entities.House

@Dao
interface CharacterDao {

  @Query("SELECT * FROM character")
  suspend fun all(): List<Character?>?

  @Query("SELECT * FROM character WHERE id IN (:characterIds)")
  suspend fun getAllByIds(vararg characterIds: String): List<Character?>?

  @Query("SELECT * FROM character WHERE id LIKE (:characterId) LIMIT 1")
  suspend fun getById(characterId: String): Character?

  @Query("SELECT * FROM character WHERE house_id LIKE :houseId")
  suspend fun findByHouseId(houseId: String): List<Character>?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAll(vararg characters: Character)

  @Delete
  suspend fun delete(character: Character)

  @Query("DELETE FROM character")
  suspend fun nukeTable()
}