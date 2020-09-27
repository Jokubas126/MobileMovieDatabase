package com.example.mmdb.ui.details.media

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MediaViewModelFactory (
    private val application: Application,
    private val movieLocalId: Int,
    private val movieRemoteId: Int
) : ViewModelProvider.AndroidViewModelFactory(application) {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return MediaViewModel(application, movieLocalId, movieRemoteId) as T
    }
}