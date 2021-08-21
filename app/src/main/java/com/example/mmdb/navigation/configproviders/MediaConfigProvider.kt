package com.example.mmdb.navigation.configproviders

import androidx.fragment.app.Fragment
import com.example.mmdb.config.requireAppConfig
import com.jokubas.mmdb.util.navigationtools.ConfigProvider
import com.jokubas.mmdb.moviedetails.ui.media.MediaConfig

class MediaConfigProvider : ConfigProvider<MediaConfig> {
    override fun config(fragment: Fragment): MediaConfig {

        val appConfig = fragment.requireAppConfig()

        val roomMovieRepository = appConfig.movieConfig.roomMovieDetailsRepository
        val remoteMovieDetailsRepository = appConfig.movieConfig.remoteMovieDetailsRepository

        return MediaConfig(
            lifecycle = fragment.lifecycle,
            provideImages = { movieId, isRemote ->
                if (isRemote)
                    remoteMovieDetailsRepository.imagesFlow(movieId = movieId)
                else roomMovieRepository.imagesFlow(movieId)
            },
            provideTrailer = { movieId, isRemote ->
                remoteMovieDetailsRepository.videoFlow(movieId = movieId)
            }
        )
    }
}