package com.example.mmdb.ui.movielists.personal.watchlist

import android.app.Application
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mmdb.R
import com.example.mmdb.config.AppConfig
import com.jokubas.mmdb.model.room.repositories.WatchlistRepository
import com.example.mmdb.ui.movielists.personal.customlists.addtolists.AddToListsPopupWindow
import com.example.mmdb.ui.movielists.personal.customlists.addtolists.AddToListsTaskManager
import com.example.mmdb.managers.ProgressManager
import com.jokubas.mmdb.model.data.entities.Movie
import com.jokubas.mmdb.model.data.entities.WatchlistMovie
import com.jokubas.mmdb.util.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WatchlistViewModel(
    application: Application,
    private val appConfig: AppConfig
) : AndroidViewModel(application) {

    private val progressManager = ProgressManager()

    private val _movies = MutableLiveData<List<Movie>>()

    val movies: LiveData<List<Movie>>
        get() = _movies
    val error: LiveData<Boolean>
        get() = progressManager.error
    val loading: LiveData<Boolean>
        get() = progressManager.loading

    private val remoteMovieRepository = appConfig.movieConfig.remoteMovieRepository
    private val watchlistRepository = WatchlistRepository(application)

    init {
        progressManager.loading()
        getWatchlist()
    }

    fun refresh() {
        progressManager.load()
        getWatchlist()
    }

    private fun getWatchlist() {
        CoroutineScope(Dispatchers.Default).launch {
            if (appConfig.networkCheckConfig.isNetworkAvailable()) {
                getMovies(watchlistRepository.getAllMovies())
            } else {
                progressManager.error()
                appConfig.networkCheckConfig.networkUnavailableNotification()
            }
        }
    }

    private fun getMovies(watchlistMovies: List<WatchlistMovie>) {
        CoroutineScope(Dispatchers.IO).launch {
            _movies.value = remoteMovieRepository.getMovieListFromWatchlists(watchlistMovies)
            if (watchlistMovies.isEmpty())
                progressManager.error()
            else progressManager.success()
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
        /*val action = WatchlistFragmentDirections.actionMovieDetails()
        action.movieRemoteId = movie.remoteId
        Navigation.findNavController(view).navigate(action)*/
    }

    // ------------ Custom lists ------------//

    fun onPlaylistAddCLicked(movie: Movie, root: View) {
        AddToListsTaskManager(
            getApplication(),
            appConfig,
            AddToListsPopupWindow(
                View.inflate(root.context, R.layout.popup_window_personal_lists_to_add, null),
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT,
                movie
            )
        )
    }
}