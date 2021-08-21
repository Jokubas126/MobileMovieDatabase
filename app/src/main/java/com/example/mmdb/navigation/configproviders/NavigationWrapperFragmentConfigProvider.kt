package com.example.mmdb.navigation.configproviders

import androidx.fragment.app.Fragment
import com.jokubas.mmdb.util.navigationtools.ConfigProvider
import com.example.mmdb.ui.NavigationWrapperFragmentConfig


class NavigationWrapperFragmentConfigProvider : ConfigProvider<NavigationWrapperFragmentConfig> {

    override fun config(fragment: Fragment) = NavigationWrapperFragmentConfig
}