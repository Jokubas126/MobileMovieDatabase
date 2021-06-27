package com.example.mmdb.navigation.configproviders

import androidx.fragment.app.Fragment
import com.example.mmdb.extensions.requireAppConfig
import com.example.mmdb.navigation.ConfigProvider
import com.example.mmdb.ui.details.innerdetails.media.MediaConfig
import com.example.mmdb.ui.details.innerdetails.media.MediaInfo
import com.jokubas.mmdb.model.data.entities.filterMainTrailer
import com.jokubas.mmdb.util.DataResponse

class MediaConfigProvider : ConfigProvider<MediaConfig> {
    override fun config(fragment: Fragment): MediaConfig {

        val appConfig = fragment.requireAppConfig()

        val roomMovieRepository = appConfig.movieConfig.roomMovieRepository
        val remoteMovieRepository = appConfig.movieConfig.remoteMovieRepository

        return MediaConfig(
            provideMediaInfo = { movieId, isRemote ->
                if (isRemote)
                    DataResponse.Success(
                        MediaInfo(
                            images = remoteMovieRepository.getImages(movieId = movieId),
                            trailer = remoteMovieRepository.getVideo(movieId = movieId)
                                .filterMainTrailer()
                        )
                    )
                else DataResponse.Success(
                        MediaInfo(
                            images = roomMovieRepository.getImagesById(movieId)
                        )
                    )
            }
        )
    }
}