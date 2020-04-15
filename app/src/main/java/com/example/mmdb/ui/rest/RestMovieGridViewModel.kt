package com.example.mmdb.ui.rest

import android.app.Application
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.lifecycle.*
import androidx.navigation.Navigation
import com.example.mmdb.NavGraphDirections
import com.example.mmdb.R
import com.example.mmdb.model.data.*
import com.example.mmdb.model.remote.repositories.RemoteMovieRepository
import com.example.mmdb.model.room.repositories.GenresRepository
import com.example.mmdb.model.room.repositories.WatchlistRepository
import com.example.mmdb.ui.personal.customlists.addtolists.AddToListsTaskManager
import com.example.mmdb.ui.personal.customlists.addtolists.AddToListsPopupWindow
import com.example.mmdb.util.*
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class RestMovieGridViewModel(application: Application, arguments: Bundle?) :
    AndroidViewModel(application), ResponseListener {

    private val _movies = MutableLiveData<List<Movie>>()
    private val _error = MutableLiveData<Boolean>()
    private val _loading = MutableLiveData<Boolean>()

    var movies: LiveData<List<Movie>> = _movies
    val error: LiveData<Boolean> = _error
    val loading: LiveData<Boolean> = _loading

    private var currentPage = 1
    private var fetchedPage = 0
    private var isListFull = false

    private lateinit var args: RestMovieGridFragmentArgs

    // repositories
    private val watchlistRepository = WatchlistRepository(application)
    private val watchlistMovieIdList = mutableListOf<Int>()
    private val genresRepository = GenresRepository(application)

    private val responseListener: ResponseListener = this

    init {
        _loading.value = true
        CoroutineScope(Dispatchers.IO).launch {
            watchlistMovieIdList.addAll(watchlistRepository.getAllMovieIds())
            arguments?.let {
                args = RestMovieGridFragmentArgs.fromBundle(it)
                getResponse()
            }
        }
    }

    fun refresh() {
        isListFull = false
        _loading.value = true
        currentPage = 1
        fetchedPage = 0
        getResponse()
    }

    fun addData() {
        if (!isListFull) {
            _loading.value = true
            currentPage++
            getResponse()
        }
    }

    // -------------------------- Remote response -----------------------------//

    private fun getResponse() {
        synchronized(this) {
            CoroutineScope(Dispatchers.IO).launch {
                if (isNetworkAvailable(getApplication())) {
                    if (currentPage - fetchedPage > 1)
                        currentPage = fetchedPage + 1
                    val response =
                        when (args.movieGridType) {
                            TYPE_MOVIE_GRID -> RemoteMovieRepository().getMovies(
                                args.keyCategory!!,
                                currentPage
                            )
                            DISCOVER_MOVIE_GRID ->
                                RemoteMovieRepository().getDiscoveredMovies(
                                    currentPage,
                                    args.startYear,
                                    args.endYear,
                                    args.genreId,
                                    args.languageKey
                                )
                            SEARCH_MOVIE_GRID -> RemoteMovieRepository().getSearchedMovies(
                                args.searchQuery!!,
                                currentPage
                            )
                            else -> null
                        }
                    responseListener.onResponseRetrieved(response)
                } else
                    withContext(Dispatchers.Main) {
                        _loading.value = false
                        networkUnavailableNotification(getApplication())
                    }
            }
        }
    }

    override fun onResponseRetrieved(response: Response<MovieResults>?) {
        response?.let { getMovieList(it) }
    }

    // -------- Data configuration -------//

    private fun getMovieList(response: Response<MovieResults>) {
        CoroutineScope(Dispatchers.IO).launch {
            if (response.isSuccessful) {
                if (currentPage == response.body()!!.totalPages)
                    isListFull = true
                formatGenres(response.body()!!.results)
            } else
                withContext(Dispatchers.Main) {
                    _loading.value = false
                    _error.value = true
                }
        }
    }

    private fun formatGenres(movieList: List<Movie>) {
        for (movie in movieList)
            movie.formatGenresString(genresRepository.getGenresByIdList(movie.genreIds))
        val finalList = checkWatchlistMovies(movieList)
        insertMovieListToData(finalList)
    }

    private fun insertMovieListToData(movieList: List<Movie>) {
        CoroutineScope(Dispatchers.Main).launch {
            if (currentPage == 1)
                _movies.value = movieList
            else {
                val tmpMovieList: MutableList<Movie> = _movies.value as MutableList
                tmpMovieList.addAll(movieList)
                _movies.value = tmpMovieList
            }
            fetchedPage = currentPage
            _loading.value = false
            _error.value = false
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