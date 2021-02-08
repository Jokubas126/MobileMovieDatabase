package com.example.mmdb.ui.details.media

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mmdb.config.AppConfig

class MediaViewModelFactory (
    private val appConfig: AppConfig,
    private val movieLocalId: Int,
    private val movieRemoteId: Int
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return MediaViewModel(appConfig, movieLocalId, movieRemoteId) as T
    }
}