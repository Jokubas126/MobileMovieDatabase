package com.example.mmdb.ui.launcher

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mmdb.config.requireAppConfig
import com.jokubas.mmdb.model.remote.repositories.RemoteMovieRepository
import com.jokubas.mmdb.model.room.repositories.GenresRepository
import com.jokubas.mmdb.util.LiveEvent
import com.jokubas.mmdb.util.isNetworkAvailable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LauncherViewModel(application: Application) : AndroidViewModel(application) {

    inner class UpdateRequired

    inner class Loaded

    val updateRequiredEvent = LiveEvent<UpdateRequired>()
    val loadedEvent = LiveEvent<Loaded>()

    private val movieRepository = application.requireAppConfig().movieConfig.remoteMovieRepository
    private val genresRepository = GenresRepository(application)

    private var isGenresLoaded: Boolean = false
    private var isGenresRequired: Boolean = false

    init {
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
        if (isGenresRequired)
            updateRequiredEvent.postValue(UpdateRequired())
        else
            loadedEvent.postValue(Loaded())
    }

    private fun checkIfAllLoaded() {
        if (isGenresLoaded)
            loadedEvent.postValue(Loaded())
    }
}