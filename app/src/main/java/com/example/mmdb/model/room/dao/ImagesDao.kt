package com.example.mmdb.model.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mmdb.model.data.Images

@Dao
interface ImagesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateImages(images: Images): Long

    @Query("SELECT * FROM images WHERE movieRoomId = :movieId")
    suspend fun getImagesById(movieId: Int): Images

    @Query("DELETE FROM images WHERE movieRoomId = :movieId")
    fun deleteImagesByMovieId(movieId: Int)
}