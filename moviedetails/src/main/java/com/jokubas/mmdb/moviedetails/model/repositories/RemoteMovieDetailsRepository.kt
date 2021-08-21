package com.jokubas.mmdb.moviedetails.model.repositories

import com.jokubas.mmdb.moviedetails.model.services.MovieDetailsService
import com.jokubas.mmdb.util.constants.MOVIE_DB_API_KEY
import com.jokubas.mmdb.util.constants.MOVIE_DB_IMAGE_LANGUAGE_EN
import com.jokubas.mmdb.util.dataResponseFlow

class RemoteMovieDetailsRepository(
    private val service: MovieDetailsService
) {

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