package com.example.moviesearcher.model.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.moviesearcher.model.data.Movie

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateMovie(movie: Movie): Long

    @Query("SELECT * FROM movie WHERE roomId = :movieId")
    fun getMovieById(movieId: Int): LiveData<Movie>

    @Query("SELECT * FROM movie WHERE roomId IN (:movieIdList)")
    fun getMoviesFromIdList(movieIdList: List<Int>): LiveData<List<Movie>>

    @Query("SELECT * FROM movie")
    fun getAllMovies(): LiveData<List<Movie>>

    @Delete
    fun deleteMovie(movie: Movie)
}