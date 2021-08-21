package com.jokubas.mmdb.moviediscover.model.services

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.jokubas.mmdb.model.data.entities.Genres
import com.jokubas.mmdb.moviediscover.model.entities.Subcategory
import com.jokubas.mmdb.util.constants.QUERY_API_KEY
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieDiscoverService {

    @GET("/3/configuration/languages")
    suspend fun languages(@Query(QUERY_API_KEY) apiKey: String): Response<List<Subcategory>>

    @GET("/3/genre/movie/list")
    suspend fun genres(@Query(QUERY_API_KEY) apiKey: String): Response<Genres>

    companion object {

        fun create(baseUrl: String, httpClient: OkHttpClient): MovieDiscoverService {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(
                    Json {
                        ignoreUnknownKeys = true
                        isLenient = true
                    }.asConverterFactory("application/json".toMediaType())
                )
                .baseUrl(baseUrl)
                .client(httpClient)
                .build()
            return retrofit.create(MovieDiscoverService::class.java)
        }
    }
}