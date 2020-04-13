package com.example.moviesearcher.ui.personal.watchlist

import android.app.Application
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.Navigation
import com.example.moviesearcher.NavGraphDirections
import com.example.moviesearcher.R
import com.example.moviesearcher.model.data.*
import com.example.moviesearcher.model.remote.repositories.RemoteMovieRepository
import com.example.moviesearcher.model.room.databases.MovieListDatabase
import com.example.moviesearcher.model.room.repositories.MovieListRepository
import com.example.moviesearcher.model.room.repositories.RoomMovieRepository
import com.example.moviesearcher.model.room.repositories.WatchlistRepository
import com.example.moviesearcher.ui.popup_windows.PersonalListsPopupWindow
import com.example.moviesearcher.util.isNetworkAvailable
import com.example.moviesearcher.util.networkUnavailableNotification
import com.example.moviesearcher.util.showProgressSnackBar
import com.example.moviesearcher.util.showToast
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WatchlistViewModel(application: Application) : AndroidViewModel(application),
    PersonalListsPopupWindow.ListsConfirmedClickListener {

    private val _movies = MutableLiveData<MutableList<Movie>>()
    private val _error = MutableLiveData<Boolean>()
    private val _loading = MutableLiveData<Boolean>()

    var movies: LiveData<MutableList<Movie>> = _movies
    val error: LiveData<Boolean> = _error
    val loading: LiveData<Boolean> = _loading

    private val movieList = mutableListOf<WatchlistMovie>()

    private val remoteMovieRepository = RemoteMovieRepository()
    private val watchlistRepository = WatchlistRepository(application)

    init {
        getWatchlist()
    }

    fun refresh() {
        _movies.value?.clear()
        getWatchlist()
    }

    private fun getWatchlist() {
        _loading.value = true
        _error.value = false
        if (movies.value.isNullOrEmpty())
        if (isNetworkAvailable(getApplication())) {
            CoroutineScope(Dispatchers.IO).launch {
                movieList.clear()
                movieList.addAll(watchlistRepository.getAllMovies())
                for (movie in movieList)
                    getMovie(movie.movieId)
                withContext(Dispatchers.Main) { _error.value = movieList.isNullOrEmpty() }
            }
        } else {
            _error.value = true
            networkUnavailableNotification(getApplication())
        }
        _loading.value = false
    }

    private fun getMovie(movieId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = remoteMovieRepository.getMovieDetails(movieId)
            withContext(Dispatchers.Main) {
                if (result.isSuccessful) {
                    val movie = result.body()
                    movie?.let {
                        it.isInWatchlist = true
                        formatGenres(it)
                    }
                }
            }
        }
    }

    private fun formatGenres(movie: Movie) {
        CoroutineScope(Dispatchers.IO).launch {
            movie.formatGenresString(movie.genres)
            insertMovieToData(movie)
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

    fun onPlaylistAddCLicked(movie: Movie, root: View) {
        val popupWindow = PersonalListsPopupWindow(
            root,
            View.inflate(root.context, R.layout.popup_window_personal_lists_to_add, null),
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT,
            movie,
            this
        )
        val movieLists =
            MovieListDatabase.getInstance(root.context).movieListDao().getAllCustomMovieLists()
        movieLists.observeForever {
            if (!it.isNullOrEmpty())
                popupWindow.setupLists(it)
        }
    }

    override fun onConfirmClicked(
        movie: Movie,
        checkedLists: List<CustomMovieList>,
        root: View
    ): Boolean {
        return when {
            checkedLists.isNullOrEmpty() -> {
                showToast(
                    getApplication(),
                    getApplication<Application>().getString(R.string.select_a_list),
                    Toast.LENGTH_SHORT
                )
                false
            }
            isNetworkAvailable(getApplication()) -> {
                CoroutineScope(Dispatchers.IO).launch {
                    val fullMovie = RemoteMovieRepository().getMovieDetails(movie.remoteId).body()
                    fullMovie?.let {
                        showProgressSnackBar(
                            root,
                            getApplication<Application>().getString(R.string.being_uploaded_to_list)
                        )
                        it.finalizeInitialization(getApplication())
                        val movieRoomId = RoomMovieRepository(getApplication())
                            .insertOrUpdateMovie(getApplication(), it)

                        for (list in checkedLists)
                            MovieListRepository(getApplication()).addMovieToMovieList(
                                list,
                                movieRoomId.toInt()
                            )
                        showSnackbarActionCheckLists(root)
                    }
                }
                true
            }
            else -> {
                networkUnavailableNotification(getApplication())
                false
            }
        }
    }

    private fun showSnackbarActionCheckLists(root: View) {
        CoroutineScope(Dispatchers.Main).launch {
            Snackbar.make(
                    root,
                    getApplication<Application>().getString(R.string.successfully_uploaded_to_list),
                    Snackbar.LENGTH_LONG
                )
                .setAction(getApplication<Application>().getString(R.string.action_check_lists)) {
                    val action = NavGraphDirections.actionGlobalCustomListsFragment()
                    Navigation.findNavController(root).navigate(action)
                }.show()
        }
    }
}