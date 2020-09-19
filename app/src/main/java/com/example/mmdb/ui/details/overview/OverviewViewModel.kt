package com.example.mmdb.ui.details.overview

import android.app.Application
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.*
import androidx.navigation.Navigation
import com.example.mmdb.MovieDetailsArgs
import com.example.mmdb.R
import com.jokubas.mmdb.model.data.dataclasses.Movie
import com.jokubas.mmdb.model.data.util.getAnyNameList
import com.jokubas.mmdb.model.remote.repositories.RemoteMovieRepository
import com.jokubas.mmdb.util.isNetworkAvailable
import com.jokubas.mmdb.util.networkUnavailableNotification
import com.jokubas.mmdb.util.stringListToListedString
import com.jokubas.mmdb.util.stringListToString
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OverviewViewModel(application: Application, arguments: Bundle?) :
    AndroidViewModel(application) {

    private var _currentMovie = MutableLiveData<Movie>()

    val currentMovie: LiveData<Movie>
        get() = _currentMovie

    private var args = arguments?.let { MovieDetailsArgs.fromBundle(it) }

    init {
        CoroutineScope(Dispatchers.IO).launch {
            args?.let {
                if (it.movieLocalId == 0)
                    getMovieDetailsRemote(it.movieRemoteId)
                else getMovieDetailsLocal(it.movieLocalId)
            }
        }
    }

    private fun getMovieDetailsRemote(movieId: Int) {
        viewModelScope.launch {
            if (isNetworkAvailable(getApplication())) {
                val movie = RemoteMovieRepository()
                    .getMovieDetails(movieId)
                movie.genresString = stringListToString(getAnyNameList(movie.genres))
                movie.productionCountryString =
                    stringListToListedString(
                        getAnyNameList(movie.productionCountryList)
                    )
                _currentMovie.value = movie
            } else {
                networkUnavailableNotification(
                    getApplication()
                )
            }
        }
    }

    private fun getMovieDetailsLocal(movieId: Int) {
        viewModelScope.launch {
            _currentMovie.value = com.jokubas.mmdb.model.room.repositories.RoomMovieRepository(
                getApplication()
            ).getMovieById(movieId)
        }
    }

    fun onNavigationItemSelected(view: View, menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.media_menu_item -> {
                args?.let {
                    val action = OverviewFragmentDirections.actionMovieMedia()
                    action.movieRemoteId = it.movieRemoteId
                    action.movieLocalId = it.movieLocalId
                    Navigation.findNavController(view).navigate(action)
                }
            }

            R.id.cast_menu_item -> {
                args?.let {
                    val action = OverviewFragmentDirections.actionMovieCredits()
                    action.movieRemoteId = it.movieRemoteId
                    action.movieLocalId = it.movieLocalId
                    Navigation.findNavController(view).navigate(action)
                }
            }
        }
        return true
    }
}