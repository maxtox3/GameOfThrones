package ru.skillbranch.gameofthrones.data.remote.api

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

object AppServiceFactory {

  fun makeMainService(isDebug: Boolean, baseUrl: String): MainApi {
    return makeDefaultRetrofit(isDebug, baseUrl).create(MainApi::class.java)
  }

  private fun makeDefaultRetrofit(
    isDebug: Boolean,
    baseUrl: String
  ): Retrofit {
    val contentType = "application/json".toMediaType()
    return Retrofit.Builder()
      .baseUrl(baseUrl)
      .client(makeHttpClient(makeLoggingInterceptor(isDebug)))
      .addConverterFactory(Json.asConverterFactory(contentType))
      .build()
  }

  private fun makeHttpClient(
    loggingInterceptor: HttpLoggingInterceptor
  ): OkHttpClient {
    return OkHttpClient.Builder()
      .addInterceptor(loggingInterceptor)
      .connectTimeout(120, TimeUnit.SECONDS)
      .readTimeout(120, TimeUnit.SECONDS)
      .build()
  }

  private fun makeLoggingInterceptor(isDebug: Boolean): HttpLoggingInterceptor {
    val logging = HttpLoggingInterceptor()
    logging.level =
      if (isDebug) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
    return logging
  }

}
