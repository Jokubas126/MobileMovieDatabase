package com.jokubas.mmdb.moviedetails.ui.credits

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jokubas.mmdb.moviedetails.actions.InnerDetailsAction

class CreditsViewModelFactory(
    private val action: InnerDetailsAction.Credits,
    private val config: CreditsConfig
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return CreditsViewModel(action, config) as T
    }
}