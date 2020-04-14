package com.example.moviesearcher.ui.rest

import com.example.moviesearcher.model.data.MovieResults
import retrofit2.Response


interface ResponseListener {
    fun onResponseRetrieved(response: Response<MovieResults>?)
}