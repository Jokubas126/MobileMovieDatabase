package com.jokubas.mmdb.model.room.dao

import androidx.room.*
import com.jokubas.mmdb.model.data.entities.WatchlistMovie
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchlistMovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateMovie(watchlistMovie: WatchlistMovie): Long

    @Query("SELECT * FROM Watchlist")
    fun getWatchlistNow(): List<WatchlistMovie>

    @Query("SELECT * FROM Watchlist")
    fun getWatchlistFlow(): Flow<List<WatchlistMovie>>

    @Query("DELETE FROM Watchlist WHERE movieId = :movieId")
    fun deleteMovieById(movieId: Int)
}