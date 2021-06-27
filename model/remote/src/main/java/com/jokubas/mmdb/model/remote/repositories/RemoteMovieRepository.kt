package com.jokubas.mmdb.model.remote.repositories

import com.jokubas.mmdb.model.data.entities.Movie
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

    /*suspend fun getMovieResults(
        page: Int,
        type: String?
    ) = type?.let { getTypeMovies(type, page) }

    suspend fun getMovieResults(
        page: Int,
        startYear: String?,
        endYear: String?,
        genreKeys: Array<String>?,
        languageKeys: Array<String>?
    ) = getDiscoveredMovies(page, startYear, endYear, genreKeys, languageKeys)

    suspend fun getMovieResults(
        page: Int,
        searchQuery: String?
    ) = searchQuery?.let { getSearchedMovies(searchQuery, page) }*/


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
        service.getCredits(
            movieId.toString(),
            MOVIE_DB_API_KEY
        )?.let { credits ->
            emit(DataResponse.Success(credits))
        } ?: DataResponse.Error("Credits not found")
    }
}