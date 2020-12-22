package com.jokubas.mmdb.model.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jokubas.mmdb.model.data.entities.Credits

@Dao
interface CreditsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateCredits(credits: Credits): Long

    @Query("SELECT * FROM credits WHERE movieRoomId = :movieId")
    suspend fun getCreditsById(movieId: Int): Credits

    @Query("DELETE FROM credits WHERE movieRoomId = :movieId")
    fun deleteCreditsByMovieId(movieId: Int)
}