package com.example.moviesearcher.model.room.dao

import androidx.room.*
import com.example.moviesearcher.model.data.Genre

@Dao
interface GenresDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateGenre(genre: Genre): Long

    @Query("SELECT * FROM genre WHERE id = :genreId")
    fun getGenreById(genreId: Int): Genre

    @Query("DELETE FROM genre")
    fun deleteAllGenres()
}