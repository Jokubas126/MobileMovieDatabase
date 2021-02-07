package com.example.mmdb.ui.details.overview

import android.app.Application
import androidx.lifecycle.*
import com.example.mmdb.config.requireAppConfig
import com.example.mmdb.managers.ProgressManager
import com.jokubas.mmdb.model.data.entities.Movie
import com.jokubas.mmdb.model.room.repositories.RoomMovieRepository
import com.jokubas.mmdb.util.DEFAULT_ID_VALUE
import com.jokubas.mmdb.util.isNetworkAvailable
import com.jokubas.mmdb.util.networkUnavailableNotification
import kotlinx.coroutines.launch

class OverviewViewModel(application: Application, movieLocalId: Int, movieRemoteId: Int) :
    AndroidViewModel(application) {

    private var _currentMovie = MutableLiveData<Movie?>()

    val currentMovie: LiveData<Movie?>
        get() = _currentMovie

    val progressManager = ProgressManager()

    private val roomMovieRepository by lazy { RoomMovieRepository(application) }
    private val remoteMovieRepository = application.requireAppConfig().movieConfig.remoteMovieRepository

    init {
        viewModelScope.launch {
            progressManager.loading()
            when (movieLocalId) {
                DEFAULT_ID_VALUE -> getRemoteMovieDetails(movieRemoteId)
                else -> getLocalMovieDetails(movieLocalId)
            }
        }
    }

    private suspend fun getRemoteMovieDetails(movieId: Int) {
        when {
            isNetworkAvailable(getApplication()) -> {
                _currentMovie.postValue(remoteMovieRepository.getMovieDetails(movieId))
                progressManager.loaded()
            }
            else -> {
                networkUnavailableNotification(getApplication())
                progressManager.error()
            }
        }
    }

    private suspend fun getLocalMovieDetails(movieId: Int) {
        roomMovieRepository.getMovieById(movieId)?.let { movie ->
            _currentMovie.postValue(movie)
            progressManager.loaded()
        } ?: progressManager.error()
    }
}