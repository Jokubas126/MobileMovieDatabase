package com.example.moviesearcher.ui.remotegrids

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class RemoteMovieGridViewModelFactory(
    private val application: Application,
    private val arguments: Bundle?
) : ViewModelProvider.AndroidViewModelFactory(application) {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return RemoteMovieGridViewModel(
            application,
            arguments
        ) as T
    }
}