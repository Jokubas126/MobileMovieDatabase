package com.example.moviesearcher.ui.grids.typegrid

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
import java.util.*

class TypeGridViewModel : ViewModel(), BaseGridViewModel {

    private val _movies = MutableLiveData<List<Movie>>()
    private val _error = MutableLiveData<Boolean>()
    private val _loading = MutableLiveData<Boolean>()

    var movies: LiveData<List<Movie>> = _movies
    val error: LiveData<Boolean> = _error
    val loading: LiveData<Boolean> = _loading

    private var page = 1
    private var isListFull = false
    private var movieListType: String? = null

    override fun fetch(args: Bundle?) {
        _error.value = false
        _loading.value = true
        isListFull = false
        if (args != null) {
            movieListType =
                    if (args.getString(KEY_MOVIE_LIST_TYPE) != null)
                        args.getString(KEY_MOVIE_LIST_TYPE)
                    else
                        KEY_POPULAR
            clearMovies()
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
        CoroutineScope(Dispatchers.IO).launch {
            val response = MovieRepository().getMovies(movieListType!!, page)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful){
                    if (page == response.body()!!.totalPages)
                        isListFull = true
                    getGenres(response.body()!!)
                }
                else {
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
        val action: NavDirections = TypeGridFragmentDirections.actionMovieDetails(movieId)
        Navigation.findNavController(view).navigate(action)
    }
}