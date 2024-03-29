package com.jokubas.mmdb.moviegrid.model.services

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.jokubas.mmdb.model.data.entities.Movie
import com.jokubas.mmdb.model.data.entities.MovieResults
import com.jokubas.mmdb.util.constants.*
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieListService {

    @GET("/3/movie/{$PATH_MOVIE_LIST_TYPE}")
    suspend fun movies(
        @Path(PATH_MOVIE_LIST_TYPE) listType: String,
        @Query(QUERY_API_KEY) apiKey: String,
        @Query(QUERY_PAGE) page: String
    ): Response<MovieResults>

    @GET("/3/movie/{$PATH_MOVIE_ID}")
    suspend fun movieById(
        @Path(PATH_MOVIE_ID) movieId: String,
        @Query(QUERY_API_KEY) apiKey: String,
        @Query(QUERY_LANGUAGE) language: String?
    ): Response<Movie>

    @GET("/3/search/movie")
    suspend fun searchedMovies(
        @Query(QUERY_API_KEY) apiKey: String,
        @Query(QUERY_SEARCH_QUERY) query: String,
        @Query(QUERY_PAGE) page: String
    ): Response<MovieResults>

    @GET("/3/discover/movie")
    suspend fun discoveredMovies(
        @Query(QUERY_API_KEY) apiKey: String,
        @Query(QUERY_PAGE) page: String,
        @Query(QUERY_RELEASE_DATE_START) startDate: String?,
        @Query(QUERY_RELEASE_DATE_END) endDate: String?,
        @Query(QUERY_GENRES) genreKeys: String?,
        @Query(QUERY_ORIGINAL_LANGUAGE) language: String?
    ): Response<MovieResults>

    companion object {

        fun create(baseUrl: String, httpClient: OkHttpClient): MovieListService {
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
            return retrofit.create(MovieListService::class.java)
        }
    }
}