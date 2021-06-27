package com.jokubas.mmdb.model.remote.repositories

import com.jokubas.mmdb.model.data.entities.Movie
import com.jokubas.mmdb.model.data.entities.MovieResults
import com.jokubas.mmdb.model.data.entities.WatchlistMovie
import com.jokubas.mmdb.model.remote.services.MovieService
import com.jokubas.mmdb.util.*
import com.jokubas.mmdb.util.constants.KEY_POPULAR
import com.jokubas.mmdb.util.constants.MOVIE_DB_API_KEY
import com.jokubas.mmdb.util.constants.MOVIE_DB_IMAGE_LANGUAGE_EN
import com.jokubas.mmdb.util.constants.MOVIE_DB_LANGUAGE_EN
import kotlinx.coroutines.flow.flow

class RemoteMovieRepository(
    private val service: MovieService
) {

    suspend fun getTypeMovies(listType: String = KEY_POPULAR, page: Int) =
        service.getMovies(listType, MOVIE_DB_API_KEY, page.toString())

    suspend fun getSearchedMovies(query: String, page: Int) =
        service.getSearchedMovies(MOVIE_DB_API_KEY, query, page.toString())

    suspend fun getDiscoveredMovies(
        page: Int,
        startYear: String?,
        endYear: String?,
        genreKeys: List<String?>?,
        languageKeys: List<String?>?
    ) = service.getDiscoveredMovies(
        MOVIE_DB_API_KEY,
        page.toString(),
        startYear?.let { "$it-01-01" },
        "$endYear-12-31",
        genreKeys?.joinToString(separator = ", "),
        languageKeys?.joinToString(separator = ", ")
    )

    suspend fun getMoviesFromIdList(ids: List<Int>): List<Movie> =
        ids.map {
            getMovieById(it)
        }

    suspend fun getMovieById(id: Int): Movie =
        service.getMovieById(
            id.toString(),
            MOVIE_DB_API_KEY,
            MOVIE_DB_LANGUAGE_EN
        )

    suspend fun getGenres() = service.getGenres(MOVIE_DB_API_KEY)

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
        service.getCredits(
            movieId.toString(),
            MOVIE_DB_API_KEY
        )?.let { credits ->
            emit(DataResponse.Success(credits))
        } ?: DataResponse.Error("Credits not found")
    }
}