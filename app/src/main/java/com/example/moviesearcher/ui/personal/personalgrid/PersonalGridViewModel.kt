package com.example.moviesearcher.ui.personal.personalgrid

import android.app.Application
import android.os.Bundle
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.Navigation
import com.example.moviesearcher.model.data.LocalMovieList
import com.example.moviesearcher.model.data.Movie
import com.example.moviesearcher.model.repositories.PersonalMovieListRepository
import com.example.moviesearcher.model.repositories.PersonalMovieRepository

class PersonalGridViewModel(application: Application) : AndroidViewModel(application) {

    private val movieRepository = PersonalMovieRepository(application)
    private val movieListRepository = PersonalMovieListRepository(getApplication())

    var movieList: LiveData<LocalMovieList>? = null

    private val _error = MutableLiveData<Boolean>()
    private val _loading = MutableLiveData<Boolean>()
    val error: LiveData<Boolean> = _error
    val loading: LiveData<Boolean> = _loading

    private lateinit var movieIdList: List<Int>

    private lateinit var args: PersonalGridFragmentArgs

    fun fetch(arguments: Bundle?) {
        if (movieList == null) {
            arguments?.let {
                args = PersonalGridFragmentArgs.fromBundle(it)
                movieIdList = args.movieIdArray.toList()
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
        val localMovieList = movieList?.value
        if (localMovieList != null) {
            movieRepository.deleteMovie(movie)
            movieListRepository.deleteMovieFromList(localMovieList, movie.roomId)
        }
    }

    fun onMovieClicked(view: View, movie: Movie) {
        val action =
            PersonalGridFragmentDirections.actionMovieDetails().setMovieLocalId(movie.roomId)
        Navigation.findNavController(view).navigate(action)
    }
}