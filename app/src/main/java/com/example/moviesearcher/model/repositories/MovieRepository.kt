package com.example.moviesearcher.model.repositories

import com.example.moviesearcher.model.remote.services.MovieApiService
import com.example.moviesearcher.util.MOVIE_DB_API_KEY
import com.example.moviesearcher.util.MOVIE_DB_IMAGE_LANGUAGE_EN
import com.example.moviesearcher.util.MOVIE_DB_LANGUAGE_EN

class MovieRepository {

    private var service = MovieApiService.api

    suspend fun getGenreMap() = service.getGenres(MOVIE_DB_API_KEY)

    suspend fun getMovies(listType: String, page: Int) = service.getMovies(listType, MOVIE_DB_API_KEY, page.toString())
    suspend fun getMovieDetails(movieId: Int) = service.getMovieDetails(movieId.toString(), MOVIE_DB_API_KEY, MOVIE_DB_LANGUAGE_EN)

    suspend fun getImages(movieId: Int) = service.getImages(movieId.toString(), MOVIE_DB_API_KEY, MOVIE_DB_IMAGE_LANGUAGE_EN)
    suspend fun getVideo(movieId: Int) = service.getVideo(movieId.toString(), MOVIE_DB_API_KEY, MOVIE_DB_IMAGE_LANGUAGE_EN)

    suspend fun getCredits(movieId: Int) = service.getCredits(movieId.toString(), MOVIE_DB_API_KEY)

    suspend fun getSearchedMovies(query: String, page: Int) = service.getSearchedMovies(MOVIE_DB_API_KEY, query, page.toString())

    suspend fun getDiscoveredMovies(
            page: Int,
            startDate: String?,
            endDate: String?,
            genreId: String?,
            languageKey: String?
    ) = service.getDiscoveredMovies(
            MOVIE_DB_API_KEY,
            page.toString(),
            startDate,
            endDate,
            genreId,
            languageKey
    )
}