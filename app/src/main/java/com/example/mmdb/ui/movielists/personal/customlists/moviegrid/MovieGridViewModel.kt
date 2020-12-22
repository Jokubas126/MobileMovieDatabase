package com.example.mmdb.ui.movielists.personal.customlists.moviegrid

import android.app.Application
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.*
import androidx.navigation.Navigation
import com.example.mmdb.R
import com.jokubas.mmdb.model.room.repositories.CustomMovieListRepository
import com.jokubas.mmdb.model.room.repositories.RoomMovieRepository
import com.jokubas.mmdb.model.room.repositories.WatchlistRepository
import com.example.mmdb.managers.ProgressManager
import com.jokubas.mmdb.model.data.entities.CustomMovieList
import com.jokubas.mmdb.model.data.entities.Movie
import com.jokubas.mmdb.model.data.entities.WatchlistMovie
import com.jokubas.mmdb.util.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MovieGridViewModel(application: Application, arguments: Bundle?) :
    AndroidViewModel(application) {

    private val progressManager = ProgressManager()
    private val args = arguments?.let { MovieGridFragmentArgs.fromBundle(arguments) }

    private var _movieList = MutableLiveData<List<Movie>>()

    private lateinit var customMovieList: CustomMovieList

    val movieList: LiveData<List<Movie>> = _movieList
    val loading: LiveData<Boolean>
        get () = progressManager.loading
    val error: LiveData<Boolean>
        get() = progressManager.error

    private var customListId = args?.movieListId?.toInt()

    private val movieRepository = RoomMovieRepository(application)
    private val movieListRepository = CustomMovieListRepository(application)
    private val watchlistRepository = WatchlistRepository(application)

    init {
        CoroutineScope(Dispatchers.Default).launch {
            progressManager.loading()
            getMovieList()
        }
    }

    fun refresh() {
        progressManager.load()
        getMovieList()
    }

    private fun getMovieList() {
        CoroutineScope(Dispatchers.IO).launch {
            customListId?.let { listId ->
                customMovieList = movieListRepository.getMovieListById(listId)
                customMovieList.movieIdList?.let { getMovies(it) }
                    ?: run { progressManager.error() }
            } ?: run { progressManager.error() }
        }
    }

    private fun getMovies(movieIdList: List<Int>) {
        viewModelScope.launch {
            _movieList.value = movieRepository.getMoviesFromIdList(movieIdList)
            if (movieIdList.isEmpty())
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

    fun deleteMovie(movie: Movie) {
        movieRepository.deleteMovieById(movie.roomId)
        movieListRepository.deleteMovieFromList(customMovieList, movie.roomId)
    }

    fun onMovieClicked(view: View, movie: Movie) {
        val action =
            MovieGridFragmentDirections.actionMovieDetails().setMovieLocalId(movie.roomId)
        Navigation.findNavController(view).navigate(action)
    }
}