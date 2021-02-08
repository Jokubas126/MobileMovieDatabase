package com.example.mmdb.ui.movielists.personal.watchlist

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mmdb.config.AppConfig

class WatchlistViewModelFactory(
    private val application: Application,
    private val appConfig: AppConfig
) : ViewModelProvider.AndroidViewModelFactory(application) {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return WatchlistViewModel(application, appConfig) as T
    }
}