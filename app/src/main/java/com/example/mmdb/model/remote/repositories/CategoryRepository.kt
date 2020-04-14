package com.example.mmdb.model.remote.repositories

import com.example.mmdb.model.remote.services.MovieApiService
import com.example.mmdb.util.MOVIE_DB_API_KEY

class CategoryRepository {

    private var service = MovieApiService.api

    suspend fun getLanguages() = service.getLanguages(MOVIE_DB_API_KEY)
    suspend fun getGenres() = service.getGenres(MOVIE_DB_API_KEY)
}