package com.example.mmdb.navigation.configproviders

import androidx.fragment.app.Fragment
import com.example.mmdb.config.AppConfig
import com.example.mmdb.extensions.requireAppConfig
import com.example.mmdb.navigation.ConfigProvider
import com.example.mmdb.ui.details.innerdetails.overview.OverviewConfig

class OverviewConfigProvider : ConfigProvider<OverviewConfig> {
    override fun config(fragment: Fragment): OverviewConfig {

        val appConfig: AppConfig = fragment.requireAppConfig()
        val remoteMovieRepository = appConfig.movieConfig.remoteMovieRepository

        return OverviewConfig(
            provideOverviewInfo = { movieId, isRemote ->
                //if(isRemote) {
                    remoteMovieRepository.movieByIdFlow(movieId)
                //}

                //TODO implement local movie data fetching
                /*roomMovieRepository.getMovieById(movieId)?.let { movie ->
                    _currentMovie.postValue(movie)
                }*/
            }
        )
    }
}