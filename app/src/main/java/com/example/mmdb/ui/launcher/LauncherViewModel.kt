package com.example.mmdb.ui.launcher

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mmdb.model.remote.repositories.RemoteMovieRepository
import com.example.mmdb.model.room.repositories.GenresRepository
import com.example.mmdb.util.isNetworkAvailable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LauncherViewModel(application: Application) : AndroidViewModel(application) {

    private val _isLoaded = MutableLiveData<Boolean>()
    val isLoaded: LiveData<Boolean> = _isLoaded

    private val _isUpdateRequired = MutableLiveData<Boolean>()
    val isUpdateRequired: LiveData<Boolean> = _isUpdateRequired

    private val movieRepository = RemoteMovieRepository()
    private val genresRepository = GenresRepository(application)

    private var isGenresLoaded: Boolean = false
    private var isGenresRequired: Boolean = false

    init {
        _isLoaded.value = false
        updateApplication()
    }

    fun updateApplication() {
        if (isNetworkAvailable(getApplication())) {
            updateGenres()
        } else
            checkForUpdates()
    }

    private fun checkForUpdates() {
        checkForGenre()
    }

    private fun checkForGenre() {
        viewModelScope.launch {
            isGenresRequired = genresRepository.getAnyGenre() == null
            checkIfAnyUpdateRequired()
        }
    }

    private fun updateGenres() {
        CoroutineScope(Dispatchers.IO).launch {
            val genres = movieRepository.getGenres()
            genresRepository.updateGenres(genres.genreList)
            isGenresLoaded = true
            checkIfAllLoaded()
        }
    }

    // done in this way for potential scaling later and checking other types of data
    private fun checkIfAnyUpdateRequired() {
        viewModelScope.launch {
            if (isGenresRequired)
                _isUpdateRequired.value = true
            else
                _isLoaded.value = true
        }
    }

    private fun checkIfAllLoaded() {
        viewModelScope.launch {
            if (isGenresLoaded)
                _isLoaded.value = true
        }
    }
}