package com.jokubas.mmdb.model.remote.services

import com.jokubas.mmdb.model.data.entities.*
import com.jokubas.mmdb.util.constants.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import okhttp3.OkHttpClient

interface MovieService {

    @GET("/3/movie/{$PATH_MOVIE_LIST_TYPE}")
    suspend fun getMovies(
        @Path(PATH_MOVIE_LIST_TYPE) listType: String, @Query(
            QUERY_API_KEY
        ) apiKey: String, @Query(QUERY_PAGE) page: String
    ): MovieResults

    @GET("/3/movie/{$PATH_MOVIE_ID}")
    suspend fun getMovieDetails(
        @Path(PATH_MOVIE_ID) movieId: String, @Query(
            QUERY_API_KEY
        ) apiKey: String, @Query(QUERY_LANGUAGE) language: String?
    ): Movie

    @GET("/3/genre/movie/list")
    suspend fun getGenres(@Query(QUERY_API_KEY) apiKey: String): Genres

    @GET("/3/movie/{$PATH_MOVIE_ID}/images")
    suspend fun getImages(
        @Path(PATH_MOVIE_ID) movieId: String, @Query(
            QUERY_API_KEY
        ) apiKey: String, @Query(QUERY_LANGUAGE) language: String?
    ): Images

    @GET("/3/movie/{$PATH_MOVIE_ID}/videos")
    suspend fun getVideo(
        @Path(PATH_MOVIE_ID) movieId: String, @Query(
            QUERY_API_KEY
        ) apiKey: String, @Query(QUERY_LANGUAGE) language: String?
    ): VideoResults

    @GET("/3/movie/{$PATH_MOVIE_ID}/credits")
    suspend fun getCredits(
        @Path(PATH_MOVIE_ID) movieId: String, @Query(
            QUERY_API_KEY
        ) apiKey: String
    ): Credits?

    @GET("/3/configuration/languages")
    suspend fun getLanguages(@Query(QUERY_API_KEY) apiKey: String): List<Subcategory>

    @GET("/3/search/movie")
    suspend fun getSearchedMovies(
        @Query(QUERY_API_KEY) apiKey: String, @Query(
            QUERY_SEARCH_QUERY
        ) query: String, @Query(QUERY_PAGE) page: String
    ): MovieResults

    @GET("/3/discover/movie")
    suspend fun getDiscoveredMovies(
        @Query(QUERY_API_KEY) apiKey: String,
        @Query(QUERY_PAGE) page: String,
        @Query(QUERY_RELEASE_DATE_START) startDate: String?,
        @Query(QUERY_RELEASE_DATE_END) endDate: String?,
        @Query(QUERY_GENRES) genreKeys: String?,
        @Query(QUERY_ORIGINAL_LANGUAGE) language: String?
    ): MovieResults

    companion object {

        fun create(baseUrl: String, httpClient: OkHttpClient): MovieService {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseUrl)
                .client(httpClient)
                .build()
            return retrofit.create(MovieService::class.java)
        }
    }


}