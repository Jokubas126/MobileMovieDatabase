package com.jokubas.mmdb.model.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jokubas.mmdb.model.data.entities.Images
import kotlinx.coroutines.flow.Flow

@Dao
interface ImagesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateImages(images: Images): Long

    @Query("SELECT * FROM images WHERE id = :movieId")
    fun images(movieId: Int): Flow<Images?>

    @Query("SELECT * FROM images WHERE id = :movieId")
    suspend fun imagesNow(movieId: Int): Images?

    @Query("DELETE FROM images WHERE id = :movieId")
    fun deleteImagesByMovieId(movieId: Int)
}