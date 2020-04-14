package com.example.mmdb.model.room.dao

import androidx.room.*
import com.example.mmdb.model.data.Genre

@Dao
interface GenresDao {

    @Transaction
    fun updateGenres(genreList: List<Genre>) {
        deleteAllGenres()
        insertAll(genreList)
    }

    @Insert
    fun insertAll(genreList: List<Genre>)

    @Query("SELECT * FROM genre LIMIT 1")
    fun getAnyGenre(): Genre?

    @Query("SELECT * FROM genre WHERE id = :genreId")
    fun getGenreById(genreId: Int): Genre

    @Query("SELECT * FROM genre WHERE id IN (:genreIdList)")
    fun getGenresByIdList(genreIdList: List<Int>): List<Genre>

    @Query("DELETE FROM genre")
    fun deleteAllGenres()
}