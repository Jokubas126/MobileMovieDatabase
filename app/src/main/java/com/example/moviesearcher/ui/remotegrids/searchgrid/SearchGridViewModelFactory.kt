package com.example.moviesearcher.ui.remotegrids.searchgrid

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SearchGridViewModelFactory(
    private val application: Application,
    private val arguments: Bundle?
) : ViewModelProvider.AndroidViewModelFactory(application) {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SearchGridViewModel(application, arguments) as T
    }
}