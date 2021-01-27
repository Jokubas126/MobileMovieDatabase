package com.example.mmdb.config

import com.jokubas.mmdb.model.remote.repositories.CategoryRepository
import com.jokubas.mmdb.model.remote.repositories.RemoteMovieRepository
import com.jokubas.mmdb.model.remote.services.MovieService
import okhttp3.OkHttpClient

class MovieConfig(
    movieServiceUrl: String,
    httpClientBuilder: OkHttpClient.Builder
) {

    val remoteMovieRepository = RemoteMovieRepository(
        service = MovieService.create(
            baseUrl = movieServiceUrl,
            httpClient = httpClientBuilder.build()
        )
    )

    val categoryRepository = CategoryRepository(
        service = MovieService.create(
            baseUrl = movieServiceUrl,
            httpClient = httpClientBuilder.build()
        )
    )
}