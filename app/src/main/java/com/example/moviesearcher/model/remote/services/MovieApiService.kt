package com.example.moviesearcher.model.remote.services

import com.example.moviesearcher.model.remote.api.MovieApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MovieApiService {
    private const val BASE_URL = "https://api.themoviedb.org"


    val api: MovieApi by lazy {
        Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(MovieApi::class.java)
    }
}