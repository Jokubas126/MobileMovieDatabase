package com.jokubas.mmdb.moviegrid.model.repositories

import com.jokubas.mmdb.model.data.entities.Movie
import com.jokubas.mmdb.model.data.entities.MovieResults
import com.jokubas.mmdb.model.data.entities.toMovieSummary
import com.jokubas.mmdb.moviegrid.model.services.MovieListService
import com.jokubas.mmdb.util.DataResponse
import com.jokubas.mmdb.util.constants.KEY_POPULAR
import com.jokubas.mmdb.util.constants.MOVIE_DB_API_KEY
import com.jokubas.mmdb.util.dataResponseFlow
import kotlinx.coroutines.flow.*
import retrofit2.Response
import kotlin.math.ceil
import kotlin.math.min
import kotlin.math.roundToInt

class RemoteMovieListRepository(
    private val service: MovieListService
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
        provideMovie: suspend (id: Int) -> Response<Movie>,
        maxPerPage: Int = Int.MAX_VALUE
    ) = flow {

        var lastPage = pageFlow.value

        emit(DataResponse.Loading())
        combine(idFlow, pageFlow) { ids, page -> ids to page }.collect { (ids, page) ->

            if (lastPage != page) {
                emit(DataResponse.Loading())
                lastPage = page
            }

            val response = when {
                ids.isEmpty() -> DataResponse.Empty()
                else -> {
                    val validMovies: List<Movie> = ids.mapNotNull { id ->
                        provideMovie.invoke(id).body()
                    }
                    when {
                        validMovies.isNotEmpty() -> DataResponse.Success(
                            MovieResults(
                                page = page,
                                movieList = validMovies.subList(
                                    page * maxPerPage - maxPerPage,
                                    min(page * maxPerPage, validMovies.size)
                                ).map { movie ->
                                    movie.toMovieSummary()
                                },
                                totalPages = ceil(validMovies.size.toDouble() / maxPerPage).roundToInt()
                            )
                        )
                        else -> DataResponse.Error()
                    }
                }
            }
            emit(response)
        }
    }
}