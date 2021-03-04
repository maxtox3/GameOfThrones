package ru.skillbranch.gameofthrones.data.remote.api

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import ru.skillbranch.gameofthrones.AppConfig.BASE_URL

object RetrofitBuilder {

  private fun getRetrofit(): Retrofit {
    val contentType = "application/json".toMediaType()
    return Retrofit.Builder()
      .baseUrl(BASE_URL)
      .addConverterFactory(Json.asConverterFactory(contentType))
      .build()
  }

  val apiService: MainApi = getRetrofit().create(MainApi::class.java)
}