package com.example.mmdb.ui.movielists.rest

import android.app.Application
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.lifecycle.*
import androidx.navigation.Navigation
import com.example.mmdb.R
import com.example.mmdb.model.data.*
import com.example.mmdb.model.remote.repositories.RemoteMovieRepository
import com.example.mmdb.model.room.repositories.GenresRepository
import com.example.mmdb.model.room.repositories.WatchlistRepository
import com.example.mmdb.ui.movielists.personal.customlists.addtolists.AddToListsTaskManager
import com.example.mmdb.ui.movielists.personal.customlists.addtolists.AddToListsPopupWindow
import com.example.mmdb.util.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class RestMovieGridViewModel(application: Application, arguments: Bundle?) :
    AndroidViewModel(application) {

    private val progressManager = RemoteListProgressManager()

    private val _movies = MutableLiveData<List<Movie>>()

    val movies: LiveData<List<Movie>>
        get() = _movies
    val error: LiveData<Boolean>
        get() = progressManager.error
    val loading: LiveData<Boolean>
        get() = progressManager.loading

    private val args = arguments?.let { RestMovieGridFragmentArgs.fromBundle(arguments) }

    // repositories
    private val remoteMovieRepository = RemoteMovieRepository()
    private val watchlistRepository = WatchlistRepository(application)
    private val genresRepository = GenresRepository(application)

    private lateinit var watchlistMovieIdList: MutableList<Int>

    init {
        viewModelScope.launch {
            progressManager.loading()
            watchlistMovieIdList = watchlistRepository.getAllMovieIds() as MutableList<Int>
            getResponse()
        }
    }

    fun refresh() {
        progressManager.refresh()
        getResponse()
    }

    fun addData() {
        if (!progressManager.isListFull) {
            progressManager.addingData()
            getResponse()
        }
    }

    private fun getResponse() {
        synchronized(this) {
            CoroutineScope(Dispatchers.IO).launch {
                if (isNetworkAvailable(getApplication())) {
                    progressManager.checkPages()
                    args?.let {
                        remoteMovieRepository.getMovieResults(
                            progressManager.currentPage,
                            args.movieGridType,
                            args.keyCategory,
                            args.startYear,
                            args.endYear,
                            args.genreId,
                            args.languageKey,
                            args.searchQuery
                        )?.let { getMovieList(it) }
                    } ?: run { progressManager.error() }
                } else {
                    progressManager.error()
                    networkUnavailableNotification(getApplication())
                }
            }
        }
    }

    private fun getMovieList(results: MovieResults) {
        progressManager.checkIfListFull(results.totalPages)
        val formattedMovieList = checkWatchlistMovies(formatGenres(results.movieList))
        insertMovieListToLiveData(formattedMovieList)
    }

    private fun formatGenres(movieList: List<Movie>): List<Movie> {
        for (movie in movieList)
            movie.formatGenresString(genresRepository.getGenresByIdList(movie.genreIds))
        return movieList
    }

    private fun checkWatchlistMovies(movieList: List<Movie>): List<Movie> {
        for (movie in movieList)
            if (watchlistMovieIdList.contains(movie.remoteId))
                movie.isInWatchlist = true
        return movieList
    }

    private fun insertMovieListToLiveData(movieList: List<Movie>) {
        viewModelScope.launch {
            if (progressManager.currentPage == 1)
                _movies.value = movieList
            else {
                val tmpMovieList = _movies.value as MutableList
                tmpMovieList.addAll(movieList)
                _movies.value = tmpMovieList
            }
            if (movieList.isEmpty())
                progressManager.error()
            else progressManager.success()
        }
    }

//------------------------ Watchlist ----------------------------//

    fun updateWatchlist(movie: Movie) {
        if (movie.isInWatchlist) {
            watchlistRepository.insertOrUpdateMovie(WatchlistMovie(movie.remoteId))
            watchlistMovieIdList.add(movie.remoteId)
            showToast(
                getApplication(),
                getApplication<Application>().getString(R.string.added_to_watchlist),
                Toast.LENGTH_SHORT
            )
        } else {
            watchlistRepository.deleteWatchlistMovie(movie.remoteId)
            watchlistMovieIdList.remove(movie.remoteId)
            showToast(
                getApplication(),
                getApplication<Application>().getString(R.string.deleted_from_watchlist),
                Toast.LENGTH_SHORT
            )
        }
    }

//------------------ Custom lists --------------------------//

    fun onPlaylistAddCLicked(movie: Movie, root: View) {
        AddToListsTaskManager(
            getApplication(),
            root,
            AddToListsPopupWindow(
                View.inflate(root.context, R.layout.popup_window_personal_lists_to_add, null),
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT,
                movie
            )
        )
    }

//------------------ Navigation -----------------------------//

    fun onMovieClicked(view: View, movie: Movie) {
        val action =
            RestMovieGridFragmentDirections.actionMovieDetails()
        action.movieRemoteId = movie.remoteId
        Navigation.findNavController(view).navigate(action)
    }
}