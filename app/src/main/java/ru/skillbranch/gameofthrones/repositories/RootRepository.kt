package ru.skillbranch.gameofthrones.repositories

import androidx.annotation.VisibleForTesting
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.skillbranch.gameofthrones.data.local.entities.CharacterFull
import ru.skillbranch.gameofthrones.data.local.entities.CharacterItem
import ru.skillbranch.gameofthrones.data.remote.api.RetrofitBuilder.apiService
import ru.skillbranch.gameofthrones.data.remote.res.CharacterRes
import ru.skillbranch.gameofthrones.data.remote.res.HouseRes
import ru.skillbranch.gameofthrones.debugLog

object RootRepository {

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
    houseNames.forEach { houseName ->
      GlobalScope.launch {
        debugLog("request for house: $houseName")
        //todo(need to get from db, if empty -> api)
        val houseInfo = apiService.getHouseInfo(houseName)
        result(houseInfo)
      }
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
    //todo(need to get from db, if empty -> api)
    houseNames.forEach { houseName ->
      GlobalScope.launch {
        val houseInfo = apiService.getHouseInfo(houseName)
//        val characters = houseInfo.forEach { house ->
//          apiService.getCharacter
//        }
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
    //TODO implement me
  }

  /**
   * Запись данных о пересонажах в DB
   * @param Characters - Список персонажей (модель CharacterRes - модель ответа из сети)
   * необходимо произвести трансформацию данных
   * @param complete - колбек о завершении вставки записей db
   */
  @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
  fun insertCharacters(Characters: List<CharacterRes>, complete: () -> Unit) {
    //TODO implement me
  }

  /**
   * При вызове данного метода необходимо выполнить удаление всех записей в db
   * @param complete - колбек о завершении очистки db
   */
  @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
  fun dropDb(complete: () -> Unit) {
    //TODO implement me
  }

  /**
   * Поиск всех персонажей по имени дома, должен вернуть список краткой информации о персонажах
   * дома - смотри модель CharacterItem
   * @param name - краткое имя дома (его первычный ключ)
   * @param result - колбек содержащий в себе список краткой информации о персонажах дома
   */
  @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
  fun findCharactersByHouseName(name: String, result: (characters: List<CharacterItem>) -> Unit) {
    //TODO implement me
  }

  /**
   * Поиск персонажа по его идентификатору, должен вернуть полную информацию о персонаже
   * и его родственных отношения - смотри модель CharacterFull
   * @param id - идентификатор персонажа
   * @param result - колбек содержащий в себе полную информацию о персонаже
   */
  @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
  fun findCharacterFullById(id: String, result: (character: CharacterFull) -> Unit) {
    //TODO implement me
  }

  /**
   * Метод возвращет true если в базе нет ни одной записи, иначе false
   * @param result - колбек о завершении очистки db
   */
  fun isNeedUpdate(result: (isNeed: Boolean) -> Unit) {
    //TODO implement me
  }

}