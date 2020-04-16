package com.example.mmdb.ui.details.overview

import android.app.Application
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.Navigation
import com.example.mmdb.MovieDetailsArgs
import com.example.mmdb.R
import com.example.mmdb.model.data.Movie
import com.example.mmdb.model.remote.repositories.RemoteMovieRepository
import com.example.mmdb.model.room.repositories.RoomMovieRepository
import com.example.mmdb.util.*
import com.example.mmdb.util.managers.ProgressManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OverviewViewModel(application: Application, arguments: Bundle?) :
    AndroidViewModel(application) {

    private val progressManager = ProgressManager()

    private val _currentMovie = MutableLiveData<Movie>()

    var currentMovie: LiveData<Movie> = _currentMovie
    val loading: LiveData<Boolean> = progressManager.loading
    val error: LiveData<Boolean> = progressManager.error

    private lateinit var args: MovieDetailsArgs

    init {
        CoroutineScope(Dispatchers.IO).launch {
            progressManager.loading()
            arguments?.let {
                args = MovieDetailsArgs.fromBundle(it)
                if (args.movieLocalId == 0)
                    getMovieDetailsRemote(args.movieRemoteId)
                else getMovieDetailsLocal(args.movieLocalId)
            }?: run{ progressManager.error() }
        }
    }

    private fun getMovieDetailsRemote(movieId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            if (isNetworkAvailable(getApplication())){
                val response = RemoteMovieRepository().getMovieDetails(movieId)
                if (response.isSuccessful) {
                    response.body()?.let {
                        it.genresString = stringListToString(getAnyNameList(it.genres))
                        it.productionCountryString =
                            stringListToListedString(getAnyNameList(it.productionCountryList))
                        withContext(Dispatchers.Main) {
                            _currentMovie.value = it
                            progressManager.retrieved()
                        }
                    }
                }
            } else {
                progressManager.error()
                networkUnavailableNotification(getApplication())
            }
        }
    }

    private fun getMovieDetailsLocal(movieId: Int) {
        currentMovie = RoomMovieRepository(getApplication()).getMovieById(movieId)
        progressManager.retrieved()
    }

    fun onNavigationItemSelected(view: View, menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.media_menu_item -> {
                val action = OverviewFragmentDirections.actionMovieMedia()
                action.movieRemoteId = args.movieRemoteId
                action.movieLocalId = args.movieLocalId
                Navigation.findNavController(view).navigate(action)
            }

            R.id.cast_menu_item -> {
                val action = OverviewFragmentDirections.actionMovieCast()
                action.movieRemoteId = args.movieRemoteId
                action.movieLocalId = args.movieLocalId
                Navigation.findNavController(view).navigate(action)
            }
        }
        return true
    }
}