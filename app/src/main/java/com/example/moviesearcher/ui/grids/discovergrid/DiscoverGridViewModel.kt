package com.example.moviesearcher.ui.grids.discovergrid

import android.app.Application
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.Navigation
import com.example.moviesearcher.R
import com.example.moviesearcher.model.data.LocalMovieList
import com.example.moviesearcher.model.data.Movie
import com.example.moviesearcher.model.data.MovieResults
import com.example.moviesearcher.model.data.WatchlistMovie
import com.example.moviesearcher.model.remote.repositories.RemoteMovieRepository
import com.example.moviesearcher.model.room.repositories.MovieListRepository
import com.example.moviesearcher.model.room.databases.MovieListDatabase
import com.example.moviesearcher.model.room.repositories.RoomMovieRepository
import com.example.moviesearcher.model.room.repositories.WatchlistRepository
import com.example.moviesearcher.ui.grids.BaseGridViewModel
import com.example.moviesearcher.ui.popup_windows.PersonalListsPopupWindow
import com.example.moviesearcher.util.*
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DiscoverGridViewModel(application: Application) : AndroidViewModel(application),
    BaseGridViewModel,
    PersonalListsPopupWindow.ListsConfirmedClickListener {

    private val _movies = MutableLiveData<List<Movie>>()
    private val _error = MutableLiveData<Boolean>()
    private val _loading = MutableLiveData<Boolean>()

    var movies: LiveData<List<Movie>> = _movies
    val error: LiveData<Boolean> = _error
    val loading: LiveData<Boolean> = _loading

    private var currentPage = 1
    private var fetchedPage = 1
    private var isListFull = false

    private var startYear: String? = null
    private var endYear: String? = null
    private var languageKey: String? = null
    private var genreId: Int = 0

    private val watchlistRepository = WatchlistRepository(application)
    private val watchlistMovieIdList = mutableListOf<Int>()

    override fun fetch(arguments: Bundle?) {
        if (movies.value.isNullOrEmpty()) {
            _error.value = false
            _loading.value = true

            arguments?.let {
                val args = DiscoverGridFragmentArgs.fromBundle(it)
                startYear = args.startYear
                endYear = args.endYear
                languageKey = args.languageKey
                genreId = args.genreId
                getMovieList()
            }
        }
    }

    override fun refresh() {
        isListFull = false
        _loading.value = true
        currentPage = 1
        getMovieList()
    }

    override fun addData() {
        if (!isListFull) {
            _loading.value = true
            currentPage++
            getMovieList()
        }
    }

    private fun getMovieList() {
        val formattedList = formatQueries()
        if (isNetworkAvailable(getApplication())) {
            configurePages()
            CoroutineScope(Dispatchers.IO).launch {
                val response = RemoteMovieRepository().getDiscoveredMovies(
                    currentPage,
                    formattedList[0],
                    formattedList[1],
                    formattedList[2],
                    languageKey
                )
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        if (currentPage == response.body()!!.totalPages)
                            isListFull = true
                        getGenres(response.body()!!)
                    } else {
                        _loading.value = false
                        _error.value = true
                    }
                }
            }
        } else {
            _loading.value = false
            networkUnavailableNotification(getApplication())
        }
    }

    private fun getGenres(movieList: MovieResults) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = RemoteMovieRepository().getGenres()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    val genres = response.body()!!
                    for (movie in movieList.results)
                        movie.formatGenresString(genres)

                    var tmpMovieList: MutableList<Movie> = _movies.value as MutableList
                    tmpMovieList.addAll(movieList.results)
                    tmpMovieList = configureWatchlistMovies(tmpMovieList)

                    _movies.value = tmpMovieList
                    fetchedPage = currentPage
                    _error.value = false
                } else
                    _error.value = true
                _loading.value = false
            }
        }
    }

    private fun formatQueries(): List<String?> {
        var startDate: String? = null
        if (!startYear.equals("∞"))
            startDate = "$startYear-01-01"
        val endDate = "$endYear-12-31"
        val genreIdString: String? =
            if (genreId == 0)
                null
            else genreId.toString()
        return listOf(startDate, endDate, genreIdString)
    }

    private fun configureWatchlistMovies(movieList: MutableList<Movie>): MutableList<Movie> {
        for (movie in movieList)
            if (watchlistMovieIdList.contains(movie.remoteId))
                movie.isInWatchlist = true
        return movieList
    }

    private fun configurePages() {
        if (currentPage == 1) {
            fetchedPage = 1
            _movies.value = mutableListOf()
        }
    }

    fun getToolbarTitle(arguments: Bundle?): String? {
        var title: String? = null
        if (arguments != null) {
            val args = DiscoverGridFragmentArgs.fromBundle(arguments)
            title = stringListToString(args.discoverNameArray.toList())
            if (title.isNullOrBlank() || title == "null")
                title = args.startYear + " - " + args.endYear
        }
        return title
    }

    override fun onMovieClicked(view: View, movie: Movie) {
        val action = DiscoverGridFragmentDirections.actionMovieDetails()
        action.movieRemoteId = movie.remoteId
        Navigation.findNavController(view).navigate(action)
    }

    fun updateWatchlist(movie: Movie) {
        if (movie.isInWatchlist) {
            watchlistRepository.insertOrUpdateMovie(WatchlistMovie(movie.remoteId))
            showToast(getApplication(), getApplication<Application>().getString(R.string.added_to_watchlist), Toast.LENGTH_SHORT)
        } else {
            watchlistRepository.deleteWatchlistMovie(movie.remoteId)
            showToast(getApplication(), getApplication<Application>().getString(R.string.deleted_from_watchlist), Toast.LENGTH_SHORT)
        }
    }

    override fun onPlaylistAddCLicked(movie: Movie, root: View) {
        val popupWindow = PersonalListsPopupWindow(
            root,
            View.inflate(root.context, R.layout.popup_window_personal_lists_to_add, null),
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT,
            movie,
            this
        )
        val movieLists =
            MovieListDatabase.getInstance(root.context).movieListDao().getAllMovieLists()
        movieLists.observeForever {
            if (!it.isNullOrEmpty())
                popupWindow.setupLists(it)
        }
    }

    override fun onConfirmClicked(movie: Movie, checkedLists: List<LocalMovieList>, root: View): Boolean {
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
                    val action = DiscoverGridFragmentDirections.actionGlobalCustomListsFragment()
                    Navigation.findNavController(root).navigate(action)
                }.show()
        }
    }
}