package com.example.moviesearcher.ui.details.overview

import android.app.Application
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.Navigation
import com.example.moviesearcher.MovieDetailsArgs
import com.example.moviesearcher.R
import com.example.moviesearcher.model.data.Movie
import com.example.moviesearcher.model.remote.repositories.MovieRepository
import com.example.moviesearcher.util.getAnyNameList
import com.example.moviesearcher.util.stringListToListedString
import com.example.moviesearcher.util.stringListToString
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OverviewViewModel(application: Application) : AndroidViewModel(application) {

    private val _currentMovie = MutableLiveData<Movie>()
    private val _loading = MutableLiveData<Boolean>()

    var currentMovie: LiveData<Movie> = _currentMovie
    val loading: LiveData<Boolean> = _loading

    private lateinit var safeArgs: MovieDetailsArgs

    fun fetch(arguments: Bundle?) {
        arguments?.let {
            _loading.value = true
            safeArgs = MovieDetailsArgs.fromBundle(it)
            if (safeArgs.movieLocalId == 0)
                getMovieDetailsRemote(safeArgs.movieRemoteId)
            else getMovieDetailsLocal(safeArgs.movieLocalId)
        }
    }

    private fun getMovieDetailsRemote(movieId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = MovieRepository()
                .getMovieDetails(movieId)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    response.body()!!.genresString =
                        stringListToString(getAnyNameList(response.body()!!.genres))
                    response.body()!!.productionCountryString =
                        stringListToListedString(getAnyNameList(response.body()!!.productionCountryList))
                    _currentMovie.value = response.body()
                    _loading.value = false
                } else {
                    _loading.value = false
                }
            }
        }
    }

    private fun getMovieDetailsLocal(movieId: Int) {
        currentMovie = com.example.moviesearcher.model.room.repositories.MovieRepository(
            getApplication()
        ).getMovieById(movieId)
        _loading.value = false
    }

    fun onNavigationItemSelected(view: View, menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.media_menu_item -> {
                val action = OverviewFragmentDirections.actionMovieMedia()
                action.movieRemoteId = safeArgs.movieRemoteId
                action.movieLocalId = safeArgs.movieLocalId
                Navigation.findNavController(view).navigate(action)
            }

            R.id.cast_menu_item -> {
                val action = OverviewFragmentDirections.actionMovieCast()
                action.movieRemoteId = safeArgs.movieRemoteId
                action.movieLocalId = safeArgs.movieLocalId
                Navigation.findNavController(view).navigate(action)
            }
        }
        return true
    }
}