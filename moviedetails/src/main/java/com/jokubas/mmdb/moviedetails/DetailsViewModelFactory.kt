package com.jokubas.mmdb.moviedetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jokubas.mmdb.moviedetails.actions.DetailsFragmentAction

class DetailsViewModelFactory(
    private val action: DetailsFragmentAction,
    private val config: DetailsFragmentConfig
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        DetailsViewModel(action, config) as T
}