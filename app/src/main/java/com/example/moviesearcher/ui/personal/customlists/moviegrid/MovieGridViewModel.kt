package com.example.moviesearcher.ui.personal.customlists.moviegrid

import android.app.Application
import android.os.Bundle
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.Navigation
import com.example.moviesearcher.model.data.LocalMovieList
import com.example.moviesearcher.model.data.Movie
import com.example.moviesearcher.model.room.repositories.MovieListRepository
import com.example.moviesearcher.model.room.repositories.MovieRepository

class MovieGridViewModel(application: Application) : AndroidViewModel(application) {

    private val movieRepository =
        MovieRepository(
            application
        )
    private val movieListRepository =
        MovieListRepository(
            getApplication()
        )

    var movieList: LiveData<LocalMovieList>? = null

    private val _error = MutableLiveData<Boolean>()
    private val _loading = MutableLiveData<Boolean>()
    val error: LiveData<Boolean> = _error
    val loading: LiveData<Boolean> = _loading

    private lateinit var args: MovieGridFragmentArgs

    fun fetch(arguments: Bundle?) {
        if (movieList == null) {
            arguments?.let {
                args = MovieGridFragmentArgs.fromBundle(it)
                getMovieList()
            }
        }
    }

    fun refresh() {
        movieList = null
        getMovieList()
    }

    private fun getMovieList() {
        movieList = movieListRepository.getMovieListById(args.movieListId.toInt())
    }

    fun movies(movieIdList: List<Int>?): LiveData<List<Movie>>? {
        _loading.value = false
        return if (movieIdList.isNullOrEmpty()) {
            _error.value = true
            null
        } else {
            _error.value = false
            movieRepository.getMoviesFromIdList(movieIdList)
        }
    }

    fun deleteMovie(movie: Movie) {
        movieRepository.deleteMovie(movie)
        val localMovieList = movieList?.value
        if (localMovieList != null)
            movieListRepository.deleteMovieFromList(localMovieList, movie.roomId)
    }

    fun onMovieClicked(view: View, movie: Movie) {
        val action =
            MovieGridFragmentDirections.actionMovieDetails().setMovieLocalId(movie.roomId)
        Navigation.findNavController(view).navigate(action)
    }
}