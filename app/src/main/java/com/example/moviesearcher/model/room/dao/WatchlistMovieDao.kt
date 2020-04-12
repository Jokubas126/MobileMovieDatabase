package com.example.moviesearcher.model.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.moviesearcher.model.data.WatchlistMovie

@Dao
interface WatchlistMovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateMovie(watchlistMovie: WatchlistMovie): Long

    @Query("SELECT * FROM watchlist_movie")
    fun getAllMovies(): List<WatchlistMovie>

    @Query("SELECT movieId FROM watchlist_movie")
    fun getAllMovieIds(): List<Int>

    @Query("DELETE FROM watchlist_movie WHERE movieId = :movieId")
    fun deleteMovieById(movieId: Int)
}