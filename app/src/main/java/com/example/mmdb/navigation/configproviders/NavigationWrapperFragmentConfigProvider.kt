package com.example.mmdb.navigation.configproviders

import androidx.fragment.app.Fragment
import com.example.mmdb.navigation.ConfigProvider
import com.example.mmdb.ui.NavigationWrapperFragmentConfig


class NavigationWrapperFragmentConfigProvider : ConfigProvider<NavigationWrapperFragmentConfig> {

    override fun config(fragment: Fragment) = NavigationWrapperFragmentConfig
}