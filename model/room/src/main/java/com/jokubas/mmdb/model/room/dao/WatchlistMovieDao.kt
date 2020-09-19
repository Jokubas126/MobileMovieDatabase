package com.jokubas.mmdb.model.room.dao

import androidx.room.*
import com.jokubas.mmdb.model.data.dataclasses.WatchlistMovie

@Dao
interface WatchlistMovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateMovie(watchlistMovie: WatchlistMovie): Long

    @Query("SELECT * FROM watchlist_movie")
    fun getAllMovies(): List<WatchlistMovie>

    @Query("SELECT movieId FROM watchlist_movie")
    suspend fun getAllMovieIds(): List<Int>

    @Query("DELETE FROM watchlist_movie WHERE movieId = :movieId")
    fun deleteMovieById(movieId: Int)
}