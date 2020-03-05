package com.example.moviesearcher.model.services

import com.example.moviesearcher.model.api.MovieApi
import com.example.moviesearcher.model.data.Movie
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