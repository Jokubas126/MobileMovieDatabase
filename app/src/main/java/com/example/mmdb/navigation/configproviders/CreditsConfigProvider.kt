package com.example.mmdb.navigation.configproviders

import androidx.fragment.app.Fragment
import com.example.mmdb.extensions.requireAppConfig
import com.example.mmdb.navigation.ConfigProvider
import com.example.mmdb.ui.details.innerdetails.credits.CreditsConfig

class CreditsConfigProvider : ConfigProvider<CreditsConfig> {
    override fun config(fragment: Fragment): CreditsConfig {

        val appConfig = fragment.requireAppConfig()

        val roomMovieRepository = appConfig.movieConfig.roomMovieRepository
        val remoteMovieRepository = appConfig.movieConfig.remoteMovieRepository

        return CreditsConfig(
            provideCreditsDataFlow = { isRemote, movieId ->
                if (isRemote) {
                    remoteMovieRepository.creditsFlow(movieId)
                } else roomMovieRepository.creditsFlow(movieId)
            }
        )
    }
}