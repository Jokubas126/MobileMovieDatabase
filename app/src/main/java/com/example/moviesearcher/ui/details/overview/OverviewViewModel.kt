package com.example.moviesearcher.ui.details.overview

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moviesearcher.model.data.Movie
import com.example.moviesearcher.model.repositories.MovieRepository
import com.example.moviesearcher.util.KEY_MOVIE_ID
import com.example.moviesearcher.util.getAnyNameList
import com.example.moviesearcher.util.stringListToListedString
import com.example.moviesearcher.util.stringListToString
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OverviewViewModel : ViewModel() {

    private val _currentMovie = MutableLiveData<Movie>()
    private val _loading = MutableLiveData<Boolean>()

    val currentMovie: LiveData<Movie> = _currentMovie
    val loading: LiveData<Boolean> = _loading

    fun fetch(arguments: Bundle?) {
        if (arguments != null) {
            _loading.value = true
            getMovieDetails(arguments.getInt(KEY_MOVIE_ID))
        }
    }

    private fun getMovieDetails(movieId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = MovieRepository().getMovieDetails(movieId)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    response.body()!!.genresString = stringListToString(getAnyNameList(response.body()!!.genres))
                    response.body()!!.productionCountryString = stringListToListedString(getAnyNameList(response.body()!!.countryList))
                    _currentMovie.value = response.body()
                    _loading.value = false
                } else {
                    _loading.value = false
                }
            }
        }
    }
}