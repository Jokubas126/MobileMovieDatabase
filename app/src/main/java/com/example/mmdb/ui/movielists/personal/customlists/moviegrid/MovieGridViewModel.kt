package com.example.mmdb.ui.movielists.personal.customlists.moviegrid

import android.app.Application
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.Navigation
import com.example.mmdb.R
import com.example.mmdb.model.data.CustomMovieList
import com.example.mmdb.model.data.Movie
import com.example.mmdb.model.data.WatchlistMovie
import com.example.mmdb.model.room.repositories.MovieListRepository
import com.example.mmdb.model.room.repositories.RoomMovieRepository
import com.example.mmdb.model.room.repositories.WatchlistRepository
import com.example.mmdb.util.managers.ProgressManager
import com.example.mmdb.util.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MovieGridViewModel(application: Application, arguments: Bundle?) :
    AndroidViewModel(application) {

    private val progressManager = ProgressManager()

    private var customMovieList: CustomMovieList? = null
    private var _movieList = MutableLiveData<List<Movie>>()

    val movieList: LiveData<List<Movie>> = _movieList
    val loading: LiveData<Boolean> = progressManager.loading
    val error: LiveData<Boolean> = progressManager.error

    private var customListId: Int? = null

    private val movieRepository = RoomMovieRepository(application)
    private val movieListRepository = MovieListRepository(application)
    private val watchlistRepository = WatchlistRepository(application)

    init {
        CoroutineScope(Dispatchers.IO).launch {
            progressManager.loading()
            arguments?.let {
                val args = MovieGridFragmentArgs.fromBundle(it)
                customListId = args.movieListId.toInt()
                getMovieList()
            }?: run{ progressManager.error() }
        }
    }

    fun refresh() {
        progressManager.load()
        getMovieList()
    }

    private fun getMovieList() {
        CoroutineScope(Dispatchers.IO).launch {
            customListId?.let {
                customMovieList = movieListRepository.getMovieListById(it)
                getMovies(customMovieList?.movieIdList)
            } ?: run { progressManager.error() }
        }
    }

    private fun getMovies(movieIdList: List<Int>?) {
        CoroutineScope(Dispatchers.IO).launch {
            if (movieIdList.isNullOrEmpty())
                progressManager.error()
            else {
                val list = movieRepository.getMoviesFromIdList(movieIdList)
                withContext(Dispatchers.Main) {
                    _movieList.value = list
                    progressManager.retrieved()
                }
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

    fun deleteMovie(movie: Movie) {
        customMovieList?.let {
            movieRepository.deleteMovieById(movie.roomId)
            movieListRepository.deleteMovieFromList(it, movie.roomId)
        }
    }

    fun onMovieClicked(view: View, movie: Movie) {
        val action =
            MovieGridFragmentDirections.actionMovieDetails().setMovieLocalId(movie.roomId)
        Navigation.findNavController(view).navigate(action)
    }
}