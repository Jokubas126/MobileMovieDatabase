package com.example.moviesearcher.model.data

import com.example.moviesearcher.util.stringListToString
import com.google.gson.annotations.SerializedName

class MovieResults {
    @SerializedName("results")
    var results: List<Movie> = listOf()

    fun formatGenres(genres: Genres){
        for (movie in results){
            val genreList = mutableListOf<String>()
            for (id in movie.genreIds){
                for(genre in genres.genreList){
                    if (genre.id == id){
                        genreList.add(genre.name)
                        break
                    }
                }
            }
            movie.genresString = stringListToString(genreList)
        }
    }
}