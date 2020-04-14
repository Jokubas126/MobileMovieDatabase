package com.example.mmdb.ui.personal.customlists.moviegrid

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MovieGridViewModelFactory(
    private val application: Application,
    private val arguments: Bundle?
): ViewModelProvider.AndroidViewModelFactory(application) {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MovieGridViewModel(application, arguments) as T
    }
}