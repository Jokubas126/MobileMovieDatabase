package com.example.mmdb.ui.movielists.moviegrid

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mmdb.navigation.actions.MovieGridFragmentAction

class MovieGridViewModelFactory(
    private val action: MovieGridFragmentAction,
    private val config: MovieGridFragmentConfig
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return MovieGridViewModel(action, config) as T
    }
}