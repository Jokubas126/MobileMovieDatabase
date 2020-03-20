package com.example.moviesearcher.model.repositories

import com.example.moviesearcher.model.remote.services.MovieApiService
import com.example.moviesearcher.util.MOVIE_DB_API_KEY

class CategoryRepository {

    private var service = MovieApiService.api

    suspend fun getLanguages() = service.getLanguages(MOVIE_DB_API_KEY)
    suspend fun getGenres() = service.getGenres(MOVIE_DB_API_KEY)
}