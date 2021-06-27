package com.jokubas.mmdb.model.room.dao

import androidx.room.*
import com.jokubas.mmdb.model.data.entities.WatchlistMovie

@Dao
interface WatchlistMovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateMovie(watchlistMovie: WatchlistMovie): Long

    @Query("SELECT * FROM Watchlist")
    fun getWatchlist(): List<WatchlistMovie>

    @Query("DELETE FROM Watchlist WHERE movieId = :movieId")
    fun deleteMovieById(movieId: Int)
}