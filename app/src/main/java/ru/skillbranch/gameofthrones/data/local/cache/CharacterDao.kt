package ru.skillbranch.gameofthrones.data.local.cache

import androidx.room.*
import ru.skillbranch.gameofthrones.data.local.entities.Character
import ru.skillbranch.gameofthrones.data.local.entities.House

@Dao
interface CharacterDao {

  @Query("SELECT * FROM character")
  suspend fun all(): List<Character?>?

  @Query("SELECT * FROM character WHERE id IN (:characterIds)")
  fun getAllByIds(vararg characterIds: String): List<Character?>?

  @Query("SELECT * FROM character WHERE id LIKE (:characterId) LIMIT 1")
  fun getById(characterId: String): Character?

  @Query("SELECT * FROM character WHERE house_id LIKE :houseId")
  fun findByHouseId(houseId: String): List<Character>?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insertAll(vararg characters: Character)

  @Delete
  fun delete(character: Character)

  @Query("DELETE FROM character")
  fun nukeTable()
}