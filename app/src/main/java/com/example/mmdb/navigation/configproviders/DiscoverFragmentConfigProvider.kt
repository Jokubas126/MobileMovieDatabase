package com.example.mmdb.navigation.configproviders

import androidx.fragment.app.Fragment
import com.example.mmdb.navigation.ConfigProvider
import com.example.mmdb.ui.discover.DiscoverFragmentConfig

class DiscoverFragmentConfigProvider: ConfigProvider<DiscoverFragmentConfig> {
    override fun config(fragment: Fragment): DiscoverFragmentConfig = DiscoverFragmentConfig()
}