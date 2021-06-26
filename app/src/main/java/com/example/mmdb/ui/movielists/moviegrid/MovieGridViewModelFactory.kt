package com.example.mmdb.ui.movielists.moviegrid

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mmdb.navigation.actions.MovieGridFragmentAction
import com.example.mmdb.ui.movielists.rest.MovieGridFragmentConfig

class MovieGridViewModelFactory(
    private val action: MovieGridFragmentAction,
    private val config: MovieGridFragmentConfig,
    private val lifecycle: Lifecycle
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return MovieGridViewModel(action, config, lifecycle) as T
    }
}