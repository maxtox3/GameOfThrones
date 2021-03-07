package ru.skillbranch.gameofthrones.data.remote.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.skillbranch.gameofthrones.data.remote.res.CharacterRes
import ru.skillbranch.gameofthrones.data.remote.res.HouseRes

interface MainApi {

  companion object {
    const val URL_HOUSES = "houses"
    const val URL_CHARACTERS = "characters"
    const val USR_BOOKS = "books"
    const val defaultPage = 1
    const val defaultPageSize = 50
  }


  @GET(URL_HOUSES)
  suspend fun getAllHouses(
    @Query("page")
    page: Int = defaultPage,
    @Query("pageSize")
    pageSize: Int = defaultPageSize
  ): List<HouseRes>

  @GET(URL_HOUSES)
  suspend fun getHouse(
    @Query("name")
    houseName: String
  ): List<HouseRes>

  @GET("${URL_CHARACTERS}/{id}")
  suspend fun getCharacter(
    @Path("id")
    id: String
  ): CharacterRes

}

