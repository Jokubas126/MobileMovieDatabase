package com.example.mmdb.ui.details.innerdetails.media

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mmdb.navigation.actions.InnerDetailsAction

class MediaViewModelFactory(
    private val action: InnerDetailsAction.MediaAction,
    private val config: MediaConfig
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return MediaViewModel(action, config) as T
    }
}