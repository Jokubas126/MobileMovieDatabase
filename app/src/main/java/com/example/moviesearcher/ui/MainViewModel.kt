package com.example.moviesearcher.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val _isLoaded = MutableLiveData<Boolean>()
    val isLoaded: LiveData<Boolean> = _isLoaded



    init {
        updateApplication()
    }

    private fun updateApplication(){

    }
}