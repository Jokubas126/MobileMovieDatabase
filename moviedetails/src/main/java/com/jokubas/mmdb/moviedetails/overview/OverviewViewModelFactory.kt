package com.jokubas.mmdb.moviedetails.overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jokubas.mmdb.moviedetails.actions.InnerDetailsAction

class OverviewViewModelFactory(
    private val action: InnerDetailsAction.Overview,
    private val config: OverviewConfig
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return OverviewViewModel(action, config) as T
    }
}