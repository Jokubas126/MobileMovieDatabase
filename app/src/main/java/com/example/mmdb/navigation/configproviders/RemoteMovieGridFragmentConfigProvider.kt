package com.example.mmdb.navigation.configproviders

import androidx.fragment.app.Fragment
import com.example.mmdb.navigation.ConfigProvider
import com.example.mmdb.ui.movielists.rest.RemoteMovieGridFragmentConfig

class RemoteMovieGridFragmentConfigProvider: ConfigProvider<RemoteMovieGridFragmentConfig> {
    override fun config(fragment: Fragment): RemoteMovieGridFragmentConfig {
        return RemoteMovieGridFragmentConfig()
    }
}