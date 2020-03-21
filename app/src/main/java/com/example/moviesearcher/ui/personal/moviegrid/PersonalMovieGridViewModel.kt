package com.example.moviesearcher.ui.personal.moviegrid

import android.app.Application
import android.os.Bundle
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.Navigation
import com.example.moviesearcher.model.data.Movie
import com.example.moviesearcher.model.repositories.PersonalMovieRepository

class PersonalMovieGridViewModel(application: Application) : AndroidViewModel(application) {

    private val _error = MutableLiveData<Boolean>()
    private val _loading = MutableLiveData<Boolean>()

    var movies: LiveData<List<Movie>>? = null
    val error: LiveData<Boolean> = _error
    val loading: LiveData<Boolean> = _loading

    private val repository = PersonalMovieRepository(application)

    private lateinit var movieIdList: List<Int>

    private lateinit var args: PersonalMovieGridFragmentArgs

    fun fetch(arguments: Bundle?){
        if (movies == null){
            arguments?.let {
                args = PersonalMovieGridFragmentArgs.fromBundle(it)
                movieIdList = args.movieIdArray.toList()
                getMovieList()
            }
        }
    }

    fun refresh(){
        movies = null
        getMovieList()
    }

    private fun getMovieList(){
        if (!movieIdList.isNullOrEmpty()){
            movies = repository.getMoviesFromIdList(movieIdList)
            _error.value = false
        } else _error.value = true
        _loading.value = false

    }

    fun onMovieClicked(view: View, movie: Movie){
        val action = PersonalMovieGridFragmentDirections.actionMovieDetails().setMovieLocalId(movie.roomId)
        Navigation.findNavController(view).navigate(action)
    }
}