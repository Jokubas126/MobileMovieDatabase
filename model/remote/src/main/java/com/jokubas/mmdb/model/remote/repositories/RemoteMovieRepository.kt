package com.jokubas.mmdb.model.remote.repositories

import com.jokubas.mmdb.model.data.entities.Movie
import com.jokubas.mmdb.model.data.entities.WatchlistMovie
import com.jokubas.mmdb.model.remote.services.MovieApiService
import com.jokubas.mmdb.util.*
import com.jokubas.mmdb.util.constants.MOVIE_DB_API_KEY
import com.jokubas.mmdb.util.constants.MOVIE_DB_IMAGE_LANGUAGE_EN
import com.jokubas.mmdb.util.constants.MOVIE_DB_LANGUAGE_EN
import kotlinx.coroutines.flow.flow

class RemoteMovieRepository {

    private var service = MovieApiService.api

    suspend fun getMovieResults(
        page: Int,
        movieListType: String?,
        type: String?,
        startYear: String?,
        endYear: String?,
        genreKeys: Array<String>?,
        languageKeys: Array<String>?,
        searchQuery: String?
    ) = when (movieListType) {
        TYPE_MOVIE_LIST -> type?.let { getTypeMovies(type, page) }
        DISCOVER_MOVIE_LIST -> getDiscoveredMovies(page, startYear, endYear, genreKeys, languageKeys)
        SEARCH_MOVIE_LIST -> searchQuery?.let { getSearchedMovies(searchQuery, page) }
        else -> null
    }

    private suspend fun getTypeMovies(listType: String, page: Int) =
        service.getMovies(listType, MOVIE_DB_API_KEY, page.toString())

    private suspend fun getSearchedMovies(query: String, page: Int) =
        service.getSearchedMovies(MOVIE_DB_API_KEY, query, page.toString())

    private suspend fun getDiscoveredMovies(
        page: Int,
        startYear: String?,
        endYear: String?,
        genreKeys: Array<String>?,
        languageKeys: Array<String>?
    ) = service.getDiscoveredMovies(
        MOVIE_DB_API_KEY,
        page.toString(),
        startYear?.let { "$it-01-01" },
        "$endYear-12-31",
        genreKeys?.joinToString(separator = ", "),
        languageKeys?.joinToString(separator = ", ")
    )

    suspend fun getMovieListFromWatchlists(watchlists: List<WatchlistMovie>): List<Movie> {
        val movieList = mutableListOf<Movie>()
        for (watchListMovie in watchlists) {
            val movie = getMovieDetails(watchListMovie.movieId)
            movie.isInWatchlist = true
            movie.formatGenresString(movie.genres)
            movieList.add(movie)
        }
        return movieList
    }

    suspend fun getGenres() = service.getGenres(MOVIE_DB_API_KEY)

    suspend fun getMovieDetails(movieId: Int) =
        service.getMovieDetails(
            movieId.toString(),
            MOVIE_DB_API_KEY,
            MOVIE_DB_LANGUAGE_EN
        )

    suspend fun getImages(movieId: Int) =
        service.getImages(
            movieId.toString(),
            MOVIE_DB_API_KEY,
            MOVIE_DB_IMAGE_LANGUAGE_EN
        )


    suspend fun getVideo(movieId: Int) =
        service.getVideo(
            movieId.toString(),
            MOVIE_DB_API_KEY,
            MOVIE_DB_IMAGE_LANGUAGE_EN
        )

    suspend fun getCredits(movieId: Int) = service.getCredits(
        movieId.toString(),
        MOVIE_DB_API_KEY
    )

    fun getCreditsFlow(movieId: Int) = flow {
        emit(
            service.getCredits(
                movieId.toString(),
                MOVIE_DB_API_KEY
            )
        )
    }
}