package com.example.mmdb.navigation.configproviders

import androidx.fragment.app.Fragment
import com.example.mmdb.extensions.requireAppConfig
import com.example.mmdb.navigation.ConfigProvider
import com.example.mmdb.ui.details.innerdetails.media.MediaConfig

class MediaConfigProvider : ConfigProvider<MediaConfig> {
    override fun config(fragment: Fragment): MediaConfig {

        val appConfig = fragment.requireAppConfig()

        val roomMovieRepository = appConfig.movieConfig.roomMovieRepository
        val remoteMovieRepository = appConfig.movieConfig.remoteMovieRepository

        return MediaConfig(
            lifecycle = fragment.lifecycle,
            provideImages = { movieId, isRemote ->
                if (isRemote)
                    remoteMovieRepository.imagesFlow(movieId = movieId)
                else roomMovieRepository.imagesFlow(movieId)
            },
            provideTrailer = { movieId, isRemote ->
                remoteMovieRepository.videoFlow(movieId = movieId)
            }
        )
    }
}