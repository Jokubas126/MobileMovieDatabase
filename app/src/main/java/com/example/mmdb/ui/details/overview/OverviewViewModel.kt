package com.example.mmdb.ui.details.overview

import android.app.Application
import androidx.lifecycle.*
import com.jokubas.mmdb.model.data.dataclasses.Movie
import com.jokubas.mmdb.model.data.util.getAnyNameList
import com.jokubas.mmdb.model.remote.repositories.RemoteMovieRepository
import com.jokubas.mmdb.model.room.repositories.RoomMovieRepository
import com.jokubas.mmdb.util.isNetworkAvailable
import com.jokubas.mmdb.util.networkUnavailableNotification
import com.jokubas.mmdb.util.stringListToListedString
import com.jokubas.mmdb.util.stringListToString
import kotlinx.coroutines.launch

class OverviewViewModel(application: Application, movieLocalId: Int, movieRemoteId: Int) :
    AndroidViewModel(application) {

    private var _currentMovie = MutableLiveData<Movie?>()

    val currentMovie: LiveData<Movie?>
        get() = _currentMovie

    init {
        viewModelScope.launch {
            when (movieLocalId) {
                0 -> getRemoteMovieDetails(movieRemoteId)
                else -> getLocalMovieDetails(movieLocalId)
            }
        }
    }

    private suspend fun getRemoteMovieDetails(movieId: Int) {
        when {
            isNetworkAvailable(getApplication()) ->
                _currentMovie.postValue(RemoteMovieRepository().getMovieDetails(movieId))

            else -> networkUnavailableNotification(getApplication())
        }
    }

    private suspend fun getLocalMovieDetails(movieId: Int) {
        _currentMovie.postValue(RoomMovieRepository(getApplication()).getMovieById(movieId))
    }
}