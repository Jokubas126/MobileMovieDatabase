package com.example.mmdb.config

import android.app.Application
import com.jokubas.mmdb.model.remote.repositories.CategoryRepository
import com.jokubas.mmdb.model.remote.repositories.RemoteMovieRepository
import com.jokubas.mmdb.model.remote.services.MovieService
import com.jokubas.mmdb.model.room.repositories.RoomMovieRepository
import okhttp3.OkHttpClient

class MovieConfig(
    application: Application,
    movieServiceUrl: String,
    httpClientBuilder: OkHttpClient.Builder
) {

    val roomMovieRepository: RoomMovieRepository by lazy {
        RoomMovieRepository(application)
    }

    val remoteMovieRepository: RemoteMovieRepository by lazy {
        RemoteMovieRepository(
            service = MovieService.create(
                baseUrl = movieServiceUrl,
                httpClient = httpClientBuilder.build()
            )
        )
    }

    val categoryRepository: CategoryRepository by lazy {
        CategoryRepository(
            service = MovieService.create(
                baseUrl = movieServiceUrl,
                httpClient = httpClientBuilder.build()
            )
        )
    }
}