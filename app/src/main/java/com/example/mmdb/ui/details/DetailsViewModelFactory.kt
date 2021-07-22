package com.example.mmdb.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mmdb.navigation.actions.DetailsFragmentAction
import com.example.mmdb.ui.ToolbarViewModel

class DetailsViewModelFactory(
    private val action: DetailsFragmentAction,
    private val config: DetailsFragmentConfig,
    private val toolbarViewModel: ToolbarViewModel
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        DetailsViewModel(action, config, toolbarViewModel) as T
}