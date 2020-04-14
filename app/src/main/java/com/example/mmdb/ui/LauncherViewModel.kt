package com.example.mmdb.ui

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mmdb.model.remote.repositories.RemoteMovieRepository
import com.example.mmdb.model.room.repositories.GenresRepository
import com.example.mmdb.util.isNetworkAvailable
import com.example.mmdb.util.showToast
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
        CoroutineScope(Dispatchers.IO).launch {
            isGenresRequired = genresRepository.getAnyGenre() == null
            checkIfAnyUpdateRequired()
        }
    }

    private fun updateGenres() {
        CoroutineScope(Dispatchers.IO).launch {
            val response = movieRepository.getGenres()
            if (response.isSuccessful) {
                val genres = response.body()
                genres?.let {
                    genresRepository.updateGenresBG(genres.genreList)
                    isGenresLoaded = true
                    checkIfAllLoaded()
                }
            } else
                checkForUpdates()
        }
    }

    // done in this way for potential scaling later and checking other types of data
    private fun checkIfAnyUpdateRequired() {
        CoroutineScope(Dispatchers.Main).launch {
            if (isGenresRequired)
                _isUpdateRequired.value = true
            else
                _isLoaded.value = true
        }
    }

    private fun checkIfAllLoaded() {
        CoroutineScope(Dispatchers.Main).launch {
            if (isGenresLoaded)
                _isLoaded.value = true
        }
    }
}