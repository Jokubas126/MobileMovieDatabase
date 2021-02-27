package com.example.mmdb.ui.details.innerdetails.credits

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mmdb.config.AppConfig

class CreditsViewModelFactory(
    private val appConfig: AppConfig,
    private val movieLocalId: Int,
    private val movieRemoteId: Int
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return CreditsViewModel(appConfig, movieLocalId, movieRemoteId) as T
    }
}