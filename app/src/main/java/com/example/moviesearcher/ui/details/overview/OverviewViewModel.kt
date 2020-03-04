package com.example.moviesearcher.ui.details.overview

import android.app.Activity
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moviesearcher.model.data.Movie
import com.example.moviesearcher.model.services.MovieDbApiService
import com.example.moviesearcher.util.BundleUtil

class OverviewViewModel: ViewModel(){

    private val _currentMovie = MutableLiveData<Movie>()
    private val _loading = MutableLiveData<Boolean>()

    val currentMovie: LiveData<Movie> = _currentMovie
    val loading: LiveData<Boolean> = _loading

    fun fetch(activity: Activity, arguments: Bundle?) {
        _loading.value = true
        Thread(Runnable {
            if (arguments != null) {
                MovieDbApiService().getMovieDetails(arguments.getInt(BundleUtil.KEY_MOVIE_ID)
                ) { retrievedMovie: Any ->
                    activity.runOnUiThread {
                        _currentMovie.value = retrievedMovie as Movie
                        _loading.setValue(false)
                    }
                }
            }
        }).start()
    }
}