package com.example.mmdb.ui.details.innerdetails.credits

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mmdb.config.AppConfig
import com.example.mmdb.navigation.actions.InnerDetailsAction

class CreditsViewModelFactory(
    private val action: InnerDetailsAction.Credits,
    private val config: CreditsConfig
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return CreditsViewModel(action, config) as T
    }
}