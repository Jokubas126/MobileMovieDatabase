package com.example.moviesearcher.model.repositories

import com.example.moviesearcher.model.services.MovieApiService
import com.example.moviesearcher.util.MOVIE_DB_API_KEY

class MovieRepository {

    private var service = MovieApiService.api

    suspend fun getGenreMap() = service.getGenreMap(MOVIE_DB_API_KEY)

    suspend fun getPopularMovies(listType: String, page: Int) = service.getMovieList(listType, MOVIE_DB_API_KEY, page.toString())
    suspend fun getMovieDetails(movieId: Int) = service.getMovieDetails(movieId.toString(), MOVIE_DB_API_KEY)

}