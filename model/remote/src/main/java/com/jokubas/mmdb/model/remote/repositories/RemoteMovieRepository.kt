package com.jokubas.mmdb.model.remote.repositories

import com.jokubas.mmdb.model.data.entities.Movie
import com.jokubas.mmdb.model.remote.services.MovieService
import com.jokubas.mmdb.util.constants.MOVIE_DB_API_KEY
import com.jokubas.mmdb.util.constants.MOVIE_DB_LANGUAGE_EN
import com.jokubas.mmdb.util.dataResponseFlow
import retrofit2.Response

class RemoteMovieRepository(
    private val service: MovieService
) {

    val movieByIdNow: suspend (id: Int) -> Response<Movie> = { id ->
        service.movie(
            id.toString(),
            MOVIE_DB_API_KEY,
            MOVIE_DB_LANGUAGE_EN
        )
    }

    suspend fun movieByIdFlow(id: Int) = dataResponseFlow {
        service.movie(
            id.toString(),
            MOVIE_DB_API_KEY,
            MOVIE_DB_LANGUAGE_EN
        )
    }

    suspend fun getGenres() = service.genres(MOVIE_DB_API_KEY)
}