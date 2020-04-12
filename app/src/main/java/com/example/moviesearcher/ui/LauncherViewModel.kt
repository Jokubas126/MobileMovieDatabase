package com.example.moviesearcher.ui

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.moviesearcher.model.remote.repositories.RemoteMovieRepository
import com.example.moviesearcher.model.room.repositories.GenresRepository
import com.example.moviesearcher.util.isNetworkAvailable
import com.example.moviesearcher.util.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LauncherViewModel(application: Application) : AndroidViewModel(application) {

    private val _isLoaded = MutableLiveData<Boolean>()
    val isLoaded: LiveData<Boolean> = _isLoaded

    private val movieRepository = RemoteMovieRepository()
    private val genresRepository = GenresRepository(application)

    private var isGenresLoaded: Boolean = false

    init {
        _isLoaded.value = false
        updateApplication()
    }

    private fun updateApplication() {
        if (isNetworkAvailable(getApplication())) {
            updateGenres()
        } else {
            showToast(getApplication(), "Application data was not updated", Toast.LENGTH_SHORT)
            _isLoaded.value = true
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
            } else {
                isGenresLoaded = true
                checkIfAllLoaded()
            }
        }
    }

    private fun checkIfAllLoaded() {
        CoroutineScope(Dispatchers.Main).launch {
            // done in this way for potential scaling later and checking other types of data
            if (isGenresLoaded)
                _isLoaded.value = true
        }
    }
}