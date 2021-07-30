package com.jokubas.mmdb.model.remote.repositories

import com.jokubas.mmdb.model.data.entities.*
import com.jokubas.mmdb.model.remote.services.MovieService
import com.jokubas.mmdb.util.DataResponse
import com.jokubas.mmdb.util.constants.KEY_POPULAR
import com.jokubas.mmdb.util.constants.MOVIE_DB_API_KEY
import com.jokubas.mmdb.util.constants.MOVIE_DB_IMAGE_LANGUAGE_EN
import com.jokubas.mmdb.util.constants.MOVIE_DB_LANGUAGE_EN
import com.jokubas.mmdb.util.dataResponseFlow
import com.jokubas.mmdb.util.toDataResponseFlow
import kotlinx.coroutines.flow.*
import retrofit2.Response
import kotlin.math.*

class RemoteMovieRepository(
    private val service: MovieService
) {

    suspend fun typeMovies(listType: String = KEY_POPULAR, pageFlow: StateFlow<Int>) =
        dataResponseFlow(pageFlow) { page ->
            service.movies(listType, MOVIE_DB_API_KEY, page.toString())
        }

    suspend fun getSearchedMovies(query: String, page: Int) =
        service.searchedMovies(MOVIE_DB_API_KEY, query, page.toString())

    suspend fun discoveredMovies(
        pageFlow: StateFlow<Int>,
        startYear: String?,
        endYear: String?,
        genreKeys: List<String?>?,
        languageKeys: List<String?>?
    ) = dataResponseFlow(pageFlow) { page ->
        service.discoveredMovies(
            MOVIE_DB_API_KEY,
            page.toString(),
            startYear?.let { "$it-01-01" },
            "$endYear-12-31",
            genreKeys?.joinToString(separator = ", "),
            languageKeys?.joinToString(separator = ", ")
        )
    }

    suspend fun moviesFromIdFlow(
        idFlow: Flow<List<Int>>,
        pageFlow: StateFlow<Int>,
        maxPerPage: Int
    ) = flow<DataResponse<MovieResults>> {

        var lastPage = pageFlow.value

        emit(DataResponse.Loading())
        combine(
            idFlow,
            pageFlow
        ) { ids, page ->
            Pair(ids, page)
        }.collect { (ids, page) ->
            if (lastPage != page) {
                emit(DataResponse.Loading())
                lastPage = page
            }

            val response = when {
                ids.isEmpty() -> DataResponse.Empty()
                else -> ids.map { movieByIdNow(it) }.takeIf { movies ->
                    movies.any { it.isSuccessful && it.body() != null }
                }?.let { movies ->
                    DataResponse.Success(
                        MovieResults(
                            page = page,
                            movieList = movies.filter { movieResponse ->
                                movieResponse.body() != null
                            }.subList(
                                page * maxPerPage - maxPerPage,
                                min(page * maxPerPage, movies.size)
                            ).mapNotNull { movieResponse ->
                                movieResponse.body()?.toMovieSummary()
                            },
                            totalPages = ceil(movies.size.toDouble() / maxPerPage).roundToInt()
                        )
                    )
                } ?: DataResponse.Error()
            }
            emit(response)
        }
    }

    private suspend fun movieByIdNow(id: Int): Response<Movie> =
        service.movieById(
            id.toString(),
            MOVIE_DB_API_KEY,
            MOVIE_DB_LANGUAGE_EN
        )

    suspend fun movieByIdFlow(id: Int) = dataResponseFlow {
        service.movieById(
            id.toString(),
            MOVIE_DB_API_KEY,
            MOVIE_DB_LANGUAGE_EN
        )
    }

    suspend fun getGenres() = service.genres(MOVIE_DB_API_KEY)

    suspend fun imagesFlow(movieId: Int) = dataResponseFlow {
        service.images(
            movieId.toString(),
            MOVIE_DB_API_KEY,
            MOVIE_DB_IMAGE_LANGUAGE_EN
        )
    }

    suspend fun videoFlow(movieId: Int) = dataResponseFlow {
        service.video(
            movieId.toString(),
            MOVIE_DB_API_KEY,
            MOVIE_DB_IMAGE_LANGUAGE_EN
        )
    }

    suspend fun getCredits(movieId: Int) = service.credits(
        movieId.toString(),
        MOVIE_DB_API_KEY
    )

    suspend fun creditsFlow(movieId: Int) = dataResponseFlow {
        service.credits(
            movieId.toString(),
            MOVIE_DB_API_KEY
        )
    }
}