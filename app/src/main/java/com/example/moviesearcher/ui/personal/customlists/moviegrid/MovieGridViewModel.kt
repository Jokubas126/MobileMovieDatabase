package com.example.moviesearcher.ui.personal.customlists.moviegrid

import android.app.Application
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.Navigation
import com.example.moviesearcher.R
import com.example.moviesearcher.model.data.CustomMovieList
import com.example.moviesearcher.model.data.Movie
import com.example.moviesearcher.model.data.WatchlistMovie
import com.example.moviesearcher.model.room.repositories.MovieListRepository
import com.example.moviesearcher.model.room.repositories.RoomMovieRepository
import com.example.moviesearcher.model.room.repositories.WatchlistRepository
import com.example.moviesearcher.util.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MovieGridViewModel(application: Application, arguments: Bundle?) : AndroidViewModel(application) {

    private var customMovieList: CustomMovieList? = null
    private var _movieList = MutableLiveData<List<Movie>>()
    val movieList: LiveData<List<Movie>> = _movieList

    private val _error = MutableLiveData<Boolean>()
    private val _loading = MutableLiveData<Boolean>()
    val error: LiveData<Boolean> = _error
    val loading: LiveData<Boolean> = _loading

    private var movieListId: Int? = null

    private val movieRepository = RoomMovieRepository(application)
    private val movieListRepository = MovieListRepository(getApplication())
    private val watchlistRepository = WatchlistRepository(application)

    init {
        _error.value = false
        arguments?.let {
            val args = MovieGridFragmentArgs.fromBundle(it)
            movieListId = args.movieListId.toInt()
        }
        getMovieList()
    }

    fun refresh() {
        getMovieList()
    }

    private fun getMovieList() {
        _loading.value = true
        CoroutineScope(Dispatchers.IO).launch {
            movieListId?.let {
                customMovieList = movieListRepository.getMovieListById(it)
                getMovies(customMovieList?.movieIdList)
            } ?: run {
                withContext(Dispatchers.Main) {
                    _loading.value = false
                    _error.value = true
                }
            }
        }
    }

    private fun getMovies(movieIdList: List<Int>?) {
        CoroutineScope(Dispatchers.IO).launch {
            if (movieIdList.isNullOrEmpty()) {
                withContext(Dispatchers.Main){
                    _error.value = true
                    _loading.value = false
                }
            } else {
                val list = movieRepository.getMoviesFromIdList(movieIdList)
                withContext(Dispatchers.Main){
                    _movieList.value = list
                    _error.value = false
                    _loading.value = false
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
            movieRepository.deleteMovie(movie)
            movieListRepository.deleteMovieFromList(it, movie.roomId)
        }
    }

    fun onMovieClicked(view: View, movie: Movie) {
        val action =
            MovieGridFragmentDirections.actionMovieDetails().setMovieLocalId(movie.roomId)
        Navigation.findNavController(view).navigate(action)
    }
}