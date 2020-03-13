package com.example.moviesearcher.ui.grids.discovergrid

import android.os.Bundle
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import com.example.moviesearcher.model.data.Movie
import com.example.moviesearcher.model.data.MovieResults
import com.example.moviesearcher.model.repositories.MovieRepository
import com.example.moviesearcher.ui.grids.BaseGridViewModel
import com.example.moviesearcher.util.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.ArrayList

class DiscoverGridViewModel : ViewModel(), BaseGridViewModel {

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

    override fun fetch(args: Bundle?) {
        if (args != null) {
            _error.value = false
            _loading.value = true
            startYear = args.getString(KEY_START_YEAR)
            endYear = args.getString(KEY_END_YEAR)
            languageKey = args.getString(KEY_LANGUAGE)
            genreId = args.getInt(KEY_GENRE_ID)
            getMovieList()
        } else {
            _error.value = true
            _loading.value = false
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

    private fun getGenres(movieResults: MovieResults) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = MovieRepository().getGenreMap()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    movieResults.formatGenres(response.body()!!)
                    _movies.value = movieResults.results
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

    override fun onMovieClicked(view: View, movieId: Int) {
        val action: NavDirections = DiscoverGridFragmentDirections.actionMovieDetails(movieId)
        Navigation.findNavController(view).navigate(action)
    }
}