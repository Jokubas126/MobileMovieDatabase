package com.jokubas.mmdb.moviediscover.ui.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class DiscoverViewModelFactory (
    private val config: DiscoverFragmentConfig
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return DiscoverViewModel(config) as T
    }
}