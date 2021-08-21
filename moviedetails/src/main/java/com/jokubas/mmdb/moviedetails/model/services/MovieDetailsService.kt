package com.jokubas.mmdb.moviedetails.model.services

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.jokubas.mmdb.model.data.entities.*
import com.jokubas.mmdb.moviedetails.model.entities.Credits
import com.jokubas.mmdb.moviedetails.model.entities.Images
import com.jokubas.mmdb.moviedetails.model.entities.VideoResults
import com.jokubas.mmdb.util.constants.*
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieDetailsService {

    @GET("/3/movie/{$PATH_MOVIE_ID}")
    suspend fun movieById(
        @Path(PATH_MOVIE_ID) movieId: String,
        @Query(QUERY_API_KEY) apiKey: String,
        @Query(QUERY_LANGUAGE) language: String?
    ): Response<Movie>

    @GET("/3/genre/movie/list")
    suspend fun genres(@Query(QUERY_API_KEY) apiKey: String): Response<Genres>

    @GET("/3/movie/{$PATH_MOVIE_ID}/images")
    suspend fun images(
        @Path(PATH_MOVIE_ID) movieId: String,
        @Query(QUERY_API_KEY) apiKey: String,
        @Query(QUERY_LANGUAGE) language: String?
    ): Response<Images>

    @GET("/3/movie/{$PATH_MOVIE_ID}/videos")
    suspend fun video(
        @Path(PATH_MOVIE_ID) movieId: String,
        @Query(QUERY_API_KEY) apiKey: String,
        @Query(QUERY_LANGUAGE) language: String?
    ): Response<VideoResults>

    @GET("/3/movie/{$PATH_MOVIE_ID}/credits")
    suspend fun credits(
        @Path(PATH_MOVIE_ID) movieId: String,
        @Query(QUERY_API_KEY) apiKey: String
    ): Response<Credits>

    companion object {

        fun create(baseUrl: String, httpClient: OkHttpClient): MovieDetailsService {
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
            return retrofit.create(MovieDetailsService::class.java)
        }
    }
}