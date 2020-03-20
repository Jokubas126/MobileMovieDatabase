package com.example.moviesearcher.ui.grids.discovergrid

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.Navigation
import com.example.moviesearcher.R
import com.example.moviesearcher.model.data.LocalMovieList
import com.example.moviesearcher.model.data.Movie
import com.example.moviesearcher.model.data.MovieResults
import com.example.moviesearcher.model.repositories.MovieRepository
import com.example.moviesearcher.model.repositories.PersonalMovieListRepository
import com.example.moviesearcher.model.repositories.PersonalMovieRepository
import com.example.moviesearcher.model.room.database.MovieListDatabase
import com.example.moviesearcher.ui.grids.BaseGridViewModel
import com.example.moviesearcher.ui.popup_windows.PersonalListsPopupWindow
import com.example.moviesearcher.util.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.ArrayList

class DiscoverGridViewModel(application: Application) : AndroidViewModel(application), BaseGridViewModel,
    PersonalListsPopupWindow.ListsConfirmedClickListener {

    private val _movies = MutableLiveData<List<Movie>>()
    private val _error = MutableLiveData<Boolean>()
    private val _loading = MutableLiveData<Boolean>()

    var movies: LiveData<List<Movie>> = _movies
    val error: LiveData<Boolean> = _error
    val loading: LiveData<Boolean> = _loading

    private var page = 1
    private var isListFull = false

    private var startYear: String? = null
    private var endYear: String? = null
    private var languageKey: String? = null
    private var genreId: Int = 0

    override fun fetch(arguments: Bundle?) {
        _error.value = false
        _loading.value = true

        arguments?.let{
            val args = DiscoverGridFragmentArgs.fromBundle(it)
            startYear = args.startYear
            endYear = args.endYear
            languageKey = args.languageKey
            genreId = args.genreId
            getMovieList()
        }
    }

    override fun refresh() {
        isListFull = false
        _error.value = false
        _loading.value = true
        page = 1
        clearMovies()
        getMovieList()
    }

    override fun addData() {
        if (!isListFull) {
            _error.value = false
            _loading.value = true
            page++
            getMovieList()
        }
    }

    private fun getMovieList() {
        val formattedList = formatQueries()
        CoroutineScope(Dispatchers.IO).launch {
            val response = MovieRepository().getDiscoveredMovies(
                page,
                formattedList[0],
                formattedList[1],
                formattedList[2],
                languageKey
            )
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    if (page == response.body()!!.totalPages)
                        isListFull = true
                    getGenres(response.body()!!)
                } else {
                    _loading.value = false
                    _error.value = true
                }
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

    private fun getGenres(movieList: MovieResults) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = MovieRepository().getGenreMap()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    movieList.formatGenres(response.body()!!)
                    _movies.value = movieList.results
                    _loading.value = false
                    _error.value = false
                } else {
                    _loading.value = false
                    _error.value = true
                }
            }
        }
    }

    private fun clearMovies() {
        _movies.value = ArrayList()
    }

    fun getToolbarTitle(args: Bundle?): String? {
        var title: String? = null
        if (args != null){
            title = stringListToString(args.getStringArray(KEY_DISCOVER_NAME_ARRAY)!!.toList())
            if (title.isNullOrBlank() || title == "null")
                title = args.getString(KEY_START_YEAR) + " - " + args.getString(KEY_END_YEAR)
        }
        return title
    }

    override fun onMovieClicked(view: View, movie: Movie) {
        val action = DiscoverGridFragmentDirections.actionMovieDetails()
        action.movieRemoteId = movie.remoteId
        Navigation.findNavController(view).navigate(action)
    }

    override fun onPlaylistAddCLicked(context: Context, movie: Movie) {
        val popupWindow = PersonalListsPopupWindow(
            context,
            View.inflate(context, R.layout.popup_window_personal_lists_to_add, null),
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT,
            movie,
            this
        )
        val movieLists = MovieListDatabase.getInstance(context).movieListDao().getAllMovieLists()
        movieLists.observeForever {
            if (!it.isNullOrEmpty())
                popupWindow.setupLists(it)
        }
    }

    override fun onConfirmClicked(checkedLists: List<LocalMovieList>, movie: Movie): Boolean {
        if (checkedLists.isEmpty())
            return false
        CoroutineScope(Dispatchers.IO).launch {
            val movieId = PersonalMovieRepository(getApplication()).insertOrUpdateMovie(movie)
            for (list in checkedLists){
                val movieListRepository = PersonalMovieListRepository(getApplication())
                val movieList = movieListRepository.getMovieListById(list.roomId)
                if (movieList!!.movieList != null)
                    (movieList.movieList as MutableList).add(movieId.toInt())
                else movieList.movieList = listOf(movieId.toInt())
                movieListRepository.insertOrUpdateMovieList(movieList)
            }
        }
        return true
    }
}