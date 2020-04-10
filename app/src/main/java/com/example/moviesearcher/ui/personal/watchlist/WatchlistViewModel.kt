package com.example.moviesearcher.ui.personal.watchlist

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.Navigation
import com.example.moviesearcher.model.data.Movie
import com.example.moviesearcher.model.data.WatchlistMovie
import com.example.moviesearcher.model.remote.repositories.MovieRepository
import com.example.moviesearcher.model.room.repositories.WatchlistRepository
import com.example.moviesearcher.util.isNetworkAvailable
import com.example.moviesearcher.util.networkUnavailableNotification
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WatchlistViewModel(application: Application) : AndroidViewModel(application) {

    private val _movies = MutableLiveData<MutableList<Movie>>()
    private val _error = MutableLiveData<Boolean>()
    private val _loading = MutableLiveData<Boolean>()

    var movies: LiveData<MutableList<Movie>> = _movies
    val error: LiveData<Boolean> = _error
    val loading: LiveData<Boolean> = _loading

    private val movieList = mutableListOf<WatchlistMovie>()

    private val remoteMovieRepository = MovieRepository()
    private val watchlistRepository = WatchlistRepository(application)

    fun fetch() {
        if (movies.value.isNullOrEmpty()) {
            _loading.value = true
            _error.value = false
            getWatchlist()
        }
    }

    private fun getWatchlist() {
        if (isNetworkAvailable(getApplication())) {
            CoroutineScope(Dispatchers.IO).launch {
                movieList.clear()
                movieList.addAll(watchlistRepository.getAllMovies())
                for (movie in movieList)
                    getMovie(movie.movieId)
                withContext(Dispatchers.Main) {
                    _loading.value = false
                    _error.value = _movies.value.isNullOrEmpty()
                }
            }
        } else {
            _loading.value = true
            networkUnavailableNotification(getApplication())
        }
    }

    private fun getMovie(movieId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = remoteMovieRepository.getMovieDetails(movieId)
            withContext(Dispatchers.Main) {
                if (result.isSuccessful) {
                    val tmpList = _movies.value
                    if (tmpList == null)
                        _movies.value = mutableListOf(result.body()!!)
                    else {
                        tmpList.add(result.body()!!)
                        _movies.value = tmpList
                    }
                }
            }
        }
    }

    fun updateWatchlist(movie: Movie) {

    }

    fun onMovieClicked(view: View, movie: Movie) {
        val action = WatchlistFragmentDirections.actionMovieDetails()
        action.movieLocalId = movie.roomId
        Navigation.findNavController(view).navigate(action)
    }
}