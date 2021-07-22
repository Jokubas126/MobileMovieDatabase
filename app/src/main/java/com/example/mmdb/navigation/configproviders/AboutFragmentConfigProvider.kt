package com.example.mmdb.navigation.configproviders

import androidx.fragment.app.Fragment
import com.example.mmdb.ui.about.AboutFragmentConfig
import com.example.mmdb.navigation.ConfigProvider

class AboutFragmentConfigProvider: ConfigProvider<AboutFragmentConfig> {
    override fun config(fragment: Fragment): AboutFragmentConfig {
        return AboutFragmentConfig()
    }
}