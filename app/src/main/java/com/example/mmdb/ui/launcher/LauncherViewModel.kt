package com.example.mmdb.ui.launcher

import androidx.lifecycle.ViewModel
import com.example.mmdb.config.AppConfig
import com.jokubas.mmdb.util.LiveEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LauncherViewModel(
    private val appConfig: AppConfig
) : ViewModel() {

    inner class UpdateRequired

    inner class Loaded

    val updateRequiredEvent = LiveEvent<UpdateRequired>()
    val loadedEvent = LiveEvent<Loaded>()

    private val movieRepository = appConfig.movieConfig.remoteMovieRepository
    //private val genresRepository = GenresRepository(application)

    private var isGenresLoaded: Boolean = false
    private var isGenresRequired: Boolean = false

    init {
        updateApplication()
    }

    fun updateApplication() {
        if (appConfig.networkCheckConfig.isNetworkAvailable()) {
            updateGenres()
        } else
            checkForUpdates()
    }

    private fun checkForUpdates() {
        checkForGenre()
    }

    private fun checkForGenre() {
        CoroutineScope(Dispatchers.IO).launch {
            //isGenresRequired = genresRepository.getAnyGenre() == null
            checkIfAnyUpdateRequired()
        }
    }

    private fun updateGenres() {
        CoroutineScope(Dispatchers.IO).launch {
            val genres = movieRepository.getGenres()
            //genresRepository.updateGenres(genres.genreList)
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