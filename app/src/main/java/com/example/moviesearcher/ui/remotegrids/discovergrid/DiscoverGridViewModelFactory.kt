package com.example.moviesearcher.ui.remotegrids.discovergrid

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class DiscoverGridViewModelFactory(
    private val application: Application,
    private val arguments: Bundle?
): ViewModelProvider.AndroidViewModelFactory(application) {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DiscoverGridViewModel(application, arguments) as T
    }
}