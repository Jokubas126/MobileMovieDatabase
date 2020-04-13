package com.example.moviesearcher.ui.remotegrids

import android.app.Application
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.lifecycle.*
import androidx.navigation.Navigation
import com.example.moviesearcher.NavGraphDirections
import com.example.moviesearcher.R
import com.example.moviesearcher.model.data.*
import com.example.moviesearcher.model.remote.repositories.RemoteMovieRepository
import com.example.moviesearcher.model.room.repositories.MovieListRepository
import com.example.moviesearcher.model.room.databases.MovieListDatabase
import com.example.moviesearcher.model.room.repositories.GenresRepository
import com.example.moviesearcher.model.room.repositories.RoomMovieRepository
import com.example.moviesearcher.model.room.repositories.WatchlistRepository
import com.example.moviesearcher.ui.popup_windows.PersonalListsPopupWindow
import com.example.moviesearcher.util.*
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.util.*

class RemoteMovieGridViewModel(application: Application, arguments: Bundle?) :
    AndroidViewModel(application),
    BaseGridViewModel, PersonalListsPopupWindow.ListsConfirmedClickListener, ResponseListener {

    private val _movies = MutableLiveData<List<Movie>>()
    private val _error = MutableLiveData<Boolean>()
    private val _loading = MutableLiveData<Boolean>()

    var movies: LiveData<List<Movie>> = _movies
    val error: LiveData<Boolean> = _error
    val loading: LiveData<Boolean> = _loading

    private var currentPage = 1
    private var fetchedPage = 0
    private var isListFull = false

    private lateinit var args: RemoteMovieGridFragmentArgs

    private lateinit var movieGridType: String

    // type
    private var movieListType: String? = null

    // discover
    private var startYear: String? = null
    private var endYear: String? = null
    private var languageKey: String? = null
    private var genreId: Int = 0

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
                retrieveArguments(it)
                getResponse()
            }
        }
    }

    private fun retrieveArguments(arguments: Bundle) {
        args = RemoteMovieGridFragmentArgs.fromBundle(arguments)
        movieGridType = args.movieGridType
        movieListType = args.keyCategory
        startYear = args.startYear
        endYear = args.endYear
        languageKey = args.languageKey
        genreId = args.genreId
    }

    override fun refresh() {
        isListFull = false
        _loading.value = true
        currentPage = 1
        getResponse()
    }

    override fun addData() {
        if (!isListFull) {
            _loading.value = true
            currentPage++
            getResponse()
        }
    }

    //------------------------- Retrieving the data --------------------------//


    private fun getResponse() {
        synchronized(this) {
            if (isNetworkAvailable(getApplication())) {
                configurePages()
                CoroutineScope(Dispatchers.IO).launch {
                    val response =
                        when (movieGridType) {
                            TYPE_MOVIE_GRID -> RemoteMovieRepository().getMovies(
                                movieListType!!,
                                currentPage
                            )
                            DISCOVER_MOVIE_GRID -> {
                                val formattedList = formatQueries()
                                RemoteMovieRepository().getDiscoveredMovies(
                                    currentPage,
                                    formattedList[0],
                                    formattedList[1],
                                    formattedList[2],
                                    languageKey
                                )
                            }
                            else -> null
                        }
                    withContext(Dispatchers.Main) { responseListener.onResponseRetrieved(response) }
                }
            } else {
                CoroutineScope(Dispatchers.Main).launch {
                    _loading.value = false
                    networkUnavailableNotification(getApplication())
                }
            }
        }
    }

    override fun onResponseRetrieved(response: Response<MovieResults>?) {
        response?.let { getMovieList(it) }
    }

    private fun getMovieList(response: Response<MovieResults>) {
        if (response.isSuccessful) {
            if (currentPage == response.body()!!.totalPages)
                isListFull = true
            formatGenres(response.body()!!.results)
        } else {
            _loading.value = false
            _error.value = true
        }
    }


    private fun formatGenres(movieList: List<Movie>) {
        CoroutineScope(Dispatchers.IO).launch {
            for (movie in movieList)
                movie.formatGenresString(genresRepository.getGenresByIdList(movie.genreIds))
            val finalList = configureWatchlistMovies(movieList)
            insertMovieListToData(finalList)
        }
    }

    private fun configureWatchlistMovies(movieList: List<Movie>): List<Movie> {
        for (movie in movieList)
            if (watchlistMovieIdList.contains(movie.remoteId))
                movie.isInWatchlist = true
        return movieList
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

    private fun configurePages() {
        if (currentPage == 1)
            fetchedPage = 0
        else if (currentPage - fetchedPage > 1)
            currentPage = fetchedPage + 1
    }


    // discover part
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
                    val fullMovie = RemoteMovieRepository()
                        .getMovieDetails(movie.remoteId).body()
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

//------------------ Navigation -----------------------------//

    override fun onMovieClicked(view: View, movie: Movie) {
        val action =
            RemoteMovieGridFragmentDirections.actionMovieDetails()
        action.movieRemoteId = movie.remoteId
        Navigation.findNavController(view).navigate(action)
    }
}