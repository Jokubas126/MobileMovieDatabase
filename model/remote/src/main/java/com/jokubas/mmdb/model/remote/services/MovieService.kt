package com.jokubas.mmdb.model.remote.services

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.jokubas.mmdb.model.data.entities.Genres
import com.jokubas.mmdb.model.data.entities.Movie
import com.jokubas.mmdb.util.constants.PATH_MOVIE_ID
import com.jokubas.mmdb.util.constants.QUERY_API_KEY
import com.jokubas.mmdb.util.constants.QUERY_LANGUAGE
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieService {

    @GET("/3/movie/{$PATH_MOVIE_ID}")
    suspend fun movie(
        @Path(PATH_MOVIE_ID) movieId: String,
        @Query(QUERY_API_KEY) apiKey: String,
        @Query(QUERY_LANGUAGE) language: String?
    ): Response<Movie>

    @GET("/3/genre/movie/list")
    suspend fun genres(@Query(QUERY_API_KEY) apiKey: String): Response<Genres>

    companion object {

        fun create(baseUrl: String, httpClient: OkHttpClient): MovieService {
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
            return retrofit.create(MovieService::class.java)
        }
    }
}