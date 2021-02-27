package com.example.mmdb.ui.details.innerdetails.overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mmdb.navigation.actions.InnerDetailsAction

class OverviewViewModelFactory(
    private val action: InnerDetailsAction.OverviewAction,
    private val config: OverviewConfig
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return OverviewViewModel(action, config) as T
    }
}