package com.example.mmdb.config

import android.app.Application
import com.jokubas.mmdb.model.remote.repositories.CategoryRepository
import com.jokubas.mmdb.model.remote.repositories.RemoteMovieRepository
import com.jokubas.mmdb.model.remote.services.MovieService
import com.jokubas.mmdb.model.room.repositories.CustomMovieListRepository
import com.jokubas.mmdb.model.room.repositories.GenresRepository
import com.jokubas.mmdb.model.room.repositories.RoomMovieRepository
import com.jokubas.mmdb.model.room.repositories.WatchlistRepository
import com.jokubas.mmdb.moviedetails.model.local.databases.CreditsDatabase
import com.jokubas.mmdb.moviedetails.model.local.databases.ImagesDatabase
import com.jokubas.mmdb.moviedetails.model.repositories.RemoteMovieDetailsRepository
import com.jokubas.mmdb.moviedetails.model.repositories.RoomMovieDetailsRepository
import com.jokubas.mmdb.moviedetails.model.services.MovieDetailsService
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

    val roomMovieDetailsRepository: RoomMovieDetailsRepository by lazy {
        RoomMovieDetailsRepository(application)
    }

    val remoteMovieDetailsRepository: RemoteMovieDetailsRepository by lazy {
        RemoteMovieDetailsRepository(
            service = MovieDetailsService.create(
                baseUrl = movieServiceUrl,
                httpClient = httpClientBuilder.build()
            )
        )
    }

    val customMovieListRepository: CustomMovieListRepository by lazy {
        CustomMovieListRepository(application)
    }

    val watchlistRepository: WatchlistRepository by lazy {
        WatchlistRepository(application)
    }

    val categoryRepository: CategoryRepository by lazy {
        CategoryRepository(
            service = MovieService.create(
                baseUrl = movieServiceUrl,
                httpClient = httpClientBuilder.build()
            )
        )
    }

    val genresRepository: GenresRepository by lazy {
        GenresRepository(application)
    }
}
