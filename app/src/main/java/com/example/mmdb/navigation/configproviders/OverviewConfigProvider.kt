package com.example.mmdb.navigation.configproviders

import androidx.fragment.app.Fragment
import com.example.mmdb.config.AppConfig
import com.example.mmdb.extensions.requireAppConfig
import com.example.mmdb.navigation.ConfigProvider
import com.example.mmdb.ui.details.DetailsMovieId
import com.example.mmdb.ui.details.overview.OverviewConfig
import com.example.mmdb.ui.details.overview.OverviewInfo
import com.jokubas.mmdb.util.DataResponse

class OverviewConfigProvider : ConfigProvider<OverviewConfig> {
    override fun config(fragment: Fragment): OverviewConfig {

        val appConfig: AppConfig = fragment.requireAppConfig()
        val remoteMovieRepository = appConfig.movieConfig.remoteMovieRepository

        return OverviewConfig(
            provideOverviewInfo = { movieId ->
                when (movieId) {
                    is DetailsMovieId.Remote -> {
                        when {
                            appConfig.networkCheckConfig.isNetworkAvailable() -> {
                                val movie = remoteMovieRepository.getMovieDetails(movieId.id)
                                DataResponse.Success(OverviewInfo(movie))
                            }
                            else -> {
                                appConfig.networkCheckConfig.networkUnavailableNotification()
                                DataResponse.Error()
                            }
                        }
                    }
                    is DetailsMovieId.Local -> {
                        /*roomMovieRepository.getMovieById(movieId)?.let { movie ->
                            _currentMovie.postValue(movie)
                        }*/
                        DataResponse.Empty
                    }
                }
            }
        )
    }
}