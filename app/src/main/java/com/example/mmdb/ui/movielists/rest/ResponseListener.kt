package com.example.mmdb.ui.movielists.rest

import com.example.mmdb.model.data.MovieResults
import retrofit2.Response


interface ResponseListener {
    fun onResponseRetrieved(response: Response<MovieResults>?)
}