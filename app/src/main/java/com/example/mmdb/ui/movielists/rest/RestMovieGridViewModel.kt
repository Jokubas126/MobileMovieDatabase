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
import retrofit2.Response

class RestMovieGridViewModel(application: Application, arguments: Bundle?) :
    AndroidViewModel(application) {

    private val progressManager = RemoteListProgressManager()

    private val _movies = MutableLiveData<List<Movie>>()

    var movies: LiveData<List<Movie>> = _movies
    val error: LiveData<Boolean> = progressManager.error
    val loading: LiveData<Boolean> = progressManager.loading

    private lateinit var args: RestMovieGridFragmentArgs

    private val watchlistMovieIdList = mutableListOf<Int>()

    // repositories
    private val watchlistRepository = WatchlistRepository(application)
    private val genresRepository = GenresRepository(application)

    init {
        CoroutineScope(Dispatchers.IO).launch {
            progressManager.load()
            watchlistMovieIdList.addAll(watchlistRepository.getAllMovieIds())
            arguments?.let {
                args = RestMovieGridFragmentArgs.fromBundle(it)
                getResponse()
            }?: run{ progressManager.error() }
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

    // -------------------------- Remote response -----------------------------//

    private fun getResponse() {
        synchronized(this) {
            CoroutineScope(Dispatchers.IO).launch {
                if (isNetworkAvailable(getApplication())) {
                    progressManager.checkPages()
                    val response =
                        when (args.movieGridType) {
                            TYPE_MOVIE_GRID -> RemoteMovieRepository().getMovies(
                                args.keyCategory!!,
                                progressManager.currentPage
                            )
                            DISCOVER_MOVIE_GRID ->
                                RemoteMovieRepository().getDiscoveredMovies(
                                    progressManager.currentPage,
                                    args.startYear,
                                    args.endYear,
                                    args.genreId,
                                    args.languageKey
                                )
                            SEARCH_MOVIE_GRID -> RemoteMovieRepository().getSearchedMovies(
                                args.searchQuery!!,
                                progressManager.currentPage
                            )
                            else -> null
                        }
                    response?.let { getMovieList(it) } ?: run { progressManager.error() }
                } else{
                    progressManager.error()
                    networkUnavailableNotification(getApplication())
                }
            }
        }
    }

    // -------- Data configuration -------//

    private fun getMovieList(response: Response<MovieResults>) {
        CoroutineScope(Dispatchers.IO).launch {
            if (response.isSuccessful) {
                progressManager.checkIfListFull(response.body()!!.totalPages)
                formatGenres(response.body()!!.results)
            } else
                progressManager.error()
        }
    }

    private fun formatGenres(movieList: List<Movie>) {
        for (movie in movieList)
            movie.formatGenresString(genresRepository.getGenresByIdList(movie.genreIds))
        val finalList = checkWatchlistMovies(movieList)
        insertMovieListToLiveData(finalList)
    }

    private fun insertMovieListToLiveData(movieList: List<Movie>) {
        CoroutineScope(Dispatchers.Main).launch {
            if (progressManager.currentPage == 1)
                _movies.value = movieList
            else {
                val tmpMovieList= _movies.value as MutableList
                tmpMovieList.addAll(movieList)
                _movies.value = tmpMovieList
            }
            progressManager.retrieved()
        }
    }

//------------------------ Watchlist ----------------------------//

    private fun checkWatchlistMovies(movieList: List<Movie>): List<Movie> {
        for (movie in movieList)
            if (watchlistMovieIdList.contains(movie.remoteId))
                movie.isInWatchlist = true
        return movieList
    }

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
            getApplication(), AddToListsPopupWindow(
                root,
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