package ru.skillbranch.gameofthrones.repositories

import androidx.annotation.VisibleForTesting
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import ru.skillbranch.gameofthrones.AppConfig
import ru.skillbranch.gameofthrones.BuildConfig
import ru.skillbranch.gameofthrones.data.*
import ru.skillbranch.gameofthrones.data.local.cache.CharacterDao
import ru.skillbranch.gameofthrones.data.local.cache.HouseDao
import ru.skillbranch.gameofthrones.data.local.entities.CharacterFull
import ru.skillbranch.gameofthrones.data.local.entities.CharacterItem
import ru.skillbranch.gameofthrones.data.remote.api.AppServiceFactory
import ru.skillbranch.gameofthrones.data.remote.res.CharacterRes
import ru.skillbranch.gameofthrones.data.remote.res.HouseRes
import ru.skillbranch.gameofthrones.debugLog
import ru.skillbranch.gameofthrones.parseId

object RootRepository {

  lateinit var houseDao: HouseDao
  lateinit var characterDao: CharacterDao

  private val apiService = AppServiceFactory.makeMainService(BuildConfig.DEBUG, AppConfig.BASE_URL)

  /**
   * Получение данных о всех домах из сети
   * @param result - колбек содержащий в себе список данных о домах
   */
  @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
  fun getAllHouses(result: (houses: List<HouseRes>) -> Unit) {
    val resultHouses = mutableListOf<HouseRes>()
    var page = 1
    fun loadHouses() {
      GlobalScope.launch {
        debugLog("request for allHouses")
        val allHouses = apiService.getAllHouses(page = page)
        when {
          allHouses.isEmpty() -> result(resultHouses)
          allHouses.isNotEmpty() -> {
            resultHouses.addAll(allHouses)
            page++
            loadHouses()
          }
        }
      }
    }
    loadHouses()
  }

  /**
   * Получение данных о требуемых домах по их полным именам из сети
   * @param houseNames - массив полных названий домов (смотри AppConfig)
   * @param result - колбек содержащий в себе список данных о домах
   */
  @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
  fun getNeedHouses(vararg houseNames: String, result: (houses: List<HouseRes>) -> Unit) {
    GlobalScope.launch {
      result(houseNames.map { async { apiService.getHouse(it).first() } }.awaitAll())
    }
  }

  /**
   * Получение данных о требуемых домах по их полным именам и персонажах в каждом из домов из сети
   * @param houseNames - массив полных названий домов (смотри AppConfig)
   * @param result - колбек содержащий в себе список данных о доме и персонажей в нем (Дом - Список Персонажей в нем)
   */
  @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
  fun getNeedHouseWithCharacters(
    vararg houseNames: String,
    result: (houses: List<Pair<HouseRes, List<CharacterRes>>>) -> Unit
  ) {
    getNeedHouses(*houseNames) { listOfHouses ->
      GlobalScope.launch {
        result(
          listOfHouses.map { houseRes ->
            async {
              houseRes to houseRes.swornMembers.map { characterUrl ->
                async { apiService.getCharacter(parseId(characterUrl)) }
              }.awaitAll()
            }
          }.awaitAll()
        )
      }
    }
  }


  /**
   * Запись данных о домах в DB
   * @param houses - Список персонажей (модель HouseRes - модель ответа из сети)
   * необходимо произвести трансформацию данных
   * @param complete - колбек о завершении вставки записей db
   */
  @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
  fun insertHouses(houses: List<HouseRes>, complete: () -> Unit) {
    GlobalScope.launch {
      houses.map(HouseRes::toHouse).toTypedArray().let {
        houseDao.insertAll(*it)
        complete()
      }
    }
  }

  /**
   * Запись данных о пересонажах в DB
   * @param Characters - Список персонажей (модель CharacterRes - модель ответа из сети)
   * необходимо произвести трансформацию данных
   * @param complete - колбек о завершении вставки записей db
   */
  @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
  fun insertCharacters(Characters: List<CharacterRes>, complete: () -> Unit) {
    GlobalScope.launch {
      characterDao.insertAll(*Characters.map(CharacterRes::toCharacter).toTypedArray())
      complete()
    }
  }

  /**
   * При вызове данного метода необходимо выполнить удаление всех записей в db
   * @param complete - колбек о завершении очистки db
   */
  @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
  fun dropDb(complete: () -> Unit) {
    GlobalScope.launch {
      characterDao.nukeTable()
      houseDao.nukeTable()
      complete()
    }
  }

  /**
   * Поиск всех персонажей по имени дома, должен вернуть список краткой информации о персонажах
   * дома - смотри модель CharacterItem
   * @param name - краткое имя дома (его первычный ключ)
   * @param result - колбек содержащий в себе список краткой информации о персонажах дома
   */
  @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
  fun findCharactersByHouseName(name: String, result: (characters: List<CharacterItem>) -> Unit) {
    val characters = mutableListOf<CharacterItem>()
    GlobalScope.launch {
      houseDao.getByName(name)?.id ?: "".let { houseId ->
        characterDao
          .findByHouseId(houseId = houseId)
          ?.map { it.toCharacterItem(name) }
          ?.let { characters.addAll(it) }
      }
    }
    result(characters)
  }

  /**
   * Поиск персонажа по его идентификатору, должен вернуть полную информацию о персонаже
   * и его родственных отношения - смотри модель CharacterFull
   * @param id - идентификатор персонажа
   * @param result - колбек содержащий в себе полную информацию о персонаже
   */
  @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
  fun findCharacterFullById(id: String, result: (character: CharacterFull) -> Unit) {
    GlobalScope.launch {
      characterDao.getById(id)?.let { character ->
        if (character.father.isNotEmpty()) debugLog("FATHER FOUND!! ${character.father}")
        if (character.mother.isNotEmpty()) debugLog("MOTHER FOUND!! ${character.mother}")
        val house = houseDao.getById(character.houseId)
        val father = characterDao.getById(parseId(character.father))?.toRelativeCharacter(house)
        val mother = characterDao.getById(parseId(character.mother))?.toRelativeCharacter(house)
        result(character.toCharacterFull(house, father = father, mother = mother))
      }
    }
  }

  /**
   * Метод возвращет true если в базе нет ни одной записи, иначе false
   * @param result - колбек о завершении очистки db
   */
  fun isNeedUpdate(result: (isNeed: Boolean) -> Unit) {
    GlobalScope.launch {
      result(houseDao.all().isNullOrEmpty())
    }
  }

}