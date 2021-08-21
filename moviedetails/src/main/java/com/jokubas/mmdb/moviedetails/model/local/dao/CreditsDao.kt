package com.jokubas.mmdb.moviedetails.model.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jokubas.mmdb.moviedetails.model.entities.Credits
import kotlinx.coroutines.flow.Flow

@Dao
interface CreditsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateCredits(credits: Credits): Long

    @Query("SELECT * FROM credits WHERE id = :movieId")
    fun credits(movieId: Int): Flow<Credits>

    @Query("SELECT * FROM credits WHERE id = :movieId")
    suspend fun creditsNow(movieId: Int): Credits?

    @Query("DELETE FROM credits WHERE id = :movieId")
    fun deleteCreditsByMovieId(movieId: Int)
}