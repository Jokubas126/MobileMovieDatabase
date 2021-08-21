package com.jokubas.mmdb.moviedetails.media

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jokubas.mmdb.moviedetails.actions.InnerDetailsAction

class MediaViewModelFactory(
    private val action: InnerDetailsAction.Media,
    private val config: MediaConfig
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return MediaViewModel(action, config) as T
    }
}