package com.example.mmdb.ui.rest

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class RestMovieGridViewModelFactory(
    private val application: Application,
    private val arguments: Bundle?
) : ViewModelProvider.AndroidViewModelFactory(application) {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return RestMovieGridViewModel(
            application,
            arguments
        ) as T
    }
}