package com.jokubas.mmdb.model.remote.services

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.jokubas.mmdb.model.data.entities.*
import com.jokubas.mmdb.util.constants.*
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import okhttp3.OkHttpClient
import retrofit2.Response

interface MovieService {

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

    @GET("/3/configuration/languages")
    suspend fun languages(@Query(QUERY_API_KEY) apiKey: String): Response<List<Subcategory>>

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

        private val contentType = "application/json".toMediaType()

        fun create(baseUrl: String, httpClient: OkHttpClient): MovieService {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(
                    Json {
                        ignoreUnknownKeys = true
                        isLenient = true
                    }.asConverterFactory(contentType)
                )
                .baseUrl(baseUrl)
                .client(httpClient)
                .build()
            return retrofit.create(MovieService::class.java)
        }
    }
}