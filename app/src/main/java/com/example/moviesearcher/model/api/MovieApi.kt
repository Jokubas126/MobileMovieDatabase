package com.example.moviesearcher.model.api

import com.example.moviesearcher.model.data.Genres
import com.example.moviesearcher.model.data.Movie
import com.example.moviesearcher.model.data.MovieResults
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {
    @GET("/3/movie/{type}")
    suspend fun getMovieList(@Path("type") listType: String, @Query("api_key") apiKey: String, @Query("page") page: String): Response<MovieResults>

    @GET("/3/movie/{movie_id}")
    suspend fun getMovieDetails(@Path("movie_id") movieId: String, @Query("api_key") apiKey: String): Response<Movie>

    @GET("/3/genre/movie/list")
    suspend fun getGenreMap(@Query("api_key") apiKey: String): Response<Genres>
}