package com.example.mmdb.ui.movielists.personal.watchlist

import android.app.Application
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.Navigation
import com.example.mmdb.R
import com.example.mmdb.model.data.*
import com.example.mmdb.model.remote.repositories.RemoteMovieRepository
import com.example.mmdb.model.room.repositories.WatchlistRepository
import com.example.mmdb.ui.movielists.personal.customlists.addtolists.AddToListsPopupWindow
import com.example.mmdb.ui.movielists.personal.customlists.addtolists.AddToListsTaskManager
import com.example.mmdb.util.isNetworkAvailable
import com.example.mmdb.util.managers.ProgressManager
import com.example.mmdb.util.networkUnavailableNotification
import com.example.mmdb.util.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WatchlistViewModel(application: Application) : AndroidViewModel(application) {

    private val progressManager = ProgressManager()

    private val _movies = MutableLiveData<MutableList<Movie>>()

    var movies: LiveData<MutableList<Movie>> = _movies
    val error: LiveData<Boolean>  = progressManager.error
    val loading: LiveData<Boolean> = progressManager.loading

    private val movieList = mutableListOf<WatchlistMovie>()

    private val remoteMovieRepository = RemoteMovieRepository()
    private val watchlistRepository = WatchlistRepository(application)

    init {
        getWatchlist()
    }

    fun refresh() {
        _movies.value = null
        getWatchlist()
    }

    private fun getWatchlist() {
        if (isNetworkAvailable(getApplication())) {
            CoroutineScope(Dispatchers.IO).launch {
                progressManager.loading()
                movieList.clear()
                movieList.addAll(watchlistRepository.getAllMovies())
                if (movieList.isEmpty())
                    progressManager.error()
                else
                    for (movie in movieList)
                        getMovie(movie.movieId)
            }
        } else {
            progressManager.error()
            networkUnavailableNotification(getApplication())
        }
    }

    private fun getMovie(movieId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = remoteMovieRepository.getMovieDetails(movieId)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        it.isInWatchlist = true
                        it.formatGenresString(it.genres)
                        insertMovieToData(it)
                    }
                }
            }
        }
    }

    private fun insertMovieToData(movie: Movie) {
        CoroutineScope(Dispatchers.Main).launch {
            val tmpList = _movies.value
            if (tmpList.isNullOrEmpty())
                _movies.value = mutableListOf(movie)
            else {
                tmpList.add(movie)
                _movies.value = tmpList
            }
        }
    }

    fun updateWatchlist(movie: Movie) {
        if (movie.isInWatchlist) {
            watchlistRepository.insertOrUpdateMovie(WatchlistMovie(movie.remoteId))
            showToast(
                getApplication(),
                getApplication<Application>().getString(R.string.added_to_watchlist),
                Toast.LENGTH_SHORT
            )
        } else {
            watchlistRepository.deleteWatchlistMovie(movie.remoteId)
            showToast(
                getApplication(),
                getApplication<Application>().getString(R.string.deleted_from_watchlist),
                Toast.LENGTH_SHORT
            )
        }
    }

    fun onMovieClicked(view: View, movie: Movie) {
        val action = WatchlistFragmentDirections.actionMovieDetails()
        action.movieRemoteId = movie.remoteId
        Navigation.findNavController(view).navigate(action)
    }

    // ------------ Custom lists ------------//

    fun onPlaylistAddCLicked(movie: Movie, root: View) {
        AddToListsTaskManager(
            getApplication(),
            AddToListsPopupWindow(
                root,
                View.inflate(root.context, R.layout.popup_window_personal_lists_to_add, null),
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT,
                movie
            )
        )
    }
}