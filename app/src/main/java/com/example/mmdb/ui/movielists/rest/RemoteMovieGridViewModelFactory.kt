package com.example.mmdb.ui.movielists.rest

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mmdb.navigation.actions.RemoteMovieGridFragmentAction

class RemoteMovieGridViewModelFactory(
    private val application: Application,
    private val action: RemoteMovieGridFragmentAction,
    private val config: RemoteMovieGridFragmentConfig,
    private val onMovieSelected: (movieRemoteId: Int) -> Unit
) : ViewModelProvider.AndroidViewModelFactory(application) {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return RemoteMovieGridViewModel(
            application,
            action,
            config,
            onMovieSelected
        ) as T
    }
}