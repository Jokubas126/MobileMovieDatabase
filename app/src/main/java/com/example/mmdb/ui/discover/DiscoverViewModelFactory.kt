package com.example.mmdb.ui.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mmdb.config.AppConfig
import com.example.mmdb.navigation.NavigationController
import com.example.mmdb.navigation.actions.DiscoverFragmentAction
import com.example.mmdb.ui.ToolbarViewModel

class DiscoverViewModelFactory (
    private val appConfig: AppConfig,
    private val navigationController: NavigationController,
    private val action: DiscoverFragmentAction,
    private val config: DiscoverFragmentConfig,
    private val toolbarViewModel: ToolbarViewModel
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return DiscoverViewModel(appConfig, navigationController, action, config, toolbarViewModel) as T
    }
}