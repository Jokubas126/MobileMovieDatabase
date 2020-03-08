package com.example.moviesearcher.ui.grids.searchgrid

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
import com.example.moviesearcher.util.KEY_SEARCH_QUERY
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.ArrayList

class SearchGridViewModel : ViewModel(), BaseGridViewModel{

    private val _movies = MutableLiveData<List<Movie>>()
    private val _error = MutableLiveData<Boolean>()
    private val _loading = MutableLiveData<Boolean>()

    var movies: LiveData<List<Movie>> = _movies
    val error: LiveData<Boolean> = _error
    val loading: LiveData<Boolean> = _loading

    private var page = 1
    private var isListFull = false
    private var searchQuery: String? = null

    override fun fetch(args: Bundle?) {
        if (args != null){
            _loading.value = true
            searchQuery = args.getString(KEY_SEARCH_QUERY)
            getMovieList()
        } else {
            _loading.value = false
            _error.value = true
        }
    }

    override fun addData() {
        if (!isListFull) {
            _error.value = false
            _loading.value = true
            page++
            getMovieList()
        }
    }

    override fun refresh() {
        _error.value = false
        _loading.value = true
        page = 1
        isListFull = false
        clearMovies()
        getMovieList()
    }

    private fun getMovieList(){
        CoroutineScope(Dispatchers.IO).launch {
            val response = MovieRepository().getSearchedMovies(searchQuery!!)
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

    override fun onMovieClicked(view: View, movieId: Int) {
        val action: NavDirections = SearchGridFragmentDirections.actionMovieDetails(movieId)
        Navigation.findNavController(view).navigate(action)
    }
}