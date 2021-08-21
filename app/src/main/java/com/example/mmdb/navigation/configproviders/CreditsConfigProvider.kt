package com.example.mmdb.navigation.configproviders

import androidx.fragment.app.Fragment
import com.example.mmdb.config.requireAppConfig
import com.jokubas.mmdb.util.navigationtools.ConfigProvider
import com.jokubas.mmdb.moviedetails.ui.credits.CreditsConfig

class CreditsConfigProvider : ConfigProvider<CreditsConfig> {
    override fun config(fragment: Fragment): CreditsConfig {

        val appConfig = fragment.requireAppConfig()

        val roomMovieDetailsRepository = appConfig.movieConfig.roomMovieDetailsRepository
        val remoteMovieDetailsRepository = appConfig.movieConfig.remoteMovieDetailsRepository

        return CreditsConfig(
            provideCreditsDataFlow = { isRemote, movieId ->
                if (isRemote) {
                    remoteMovieDetailsRepository.creditsFlow(movieId)
                } else roomMovieDetailsRepository.creditsFlow(movieId)
            }
        )
    }
}