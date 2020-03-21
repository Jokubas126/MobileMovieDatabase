package com.example.moviesearcher.model.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.moviesearcher.model.data.Images

@Dao
interface ImagesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateImages(images: Images): Long

    @Query("SELECT * FROM images WHERE movieRoomId = :movieId")
    fun getImagesById(movieId: Int): LiveData<Images>

    @Query("DELETE FROM images WHERE movieRoomId = :movieId")
    fun deleteImagesByMovieId(movieId: Int)
}