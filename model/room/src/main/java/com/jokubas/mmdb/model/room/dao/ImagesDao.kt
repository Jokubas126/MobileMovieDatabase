package com.jokubas.mmdb.model.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jokubas.mmdb.model.data.entities.Images

@Dao
interface ImagesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateImages(images: Images): Long

    @Query("SELECT * FROM images WHERE movieRoomId = :movieId")
    suspend fun getImagesById(movieId: Int): Images

    @Query("DELETE FROM images WHERE movieRoomId = :movieId")
    fun deleteImagesByMovieId(movieId: Int)
}