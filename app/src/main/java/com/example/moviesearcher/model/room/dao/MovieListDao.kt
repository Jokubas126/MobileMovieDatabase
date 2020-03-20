package com.example.moviesearcher.model.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.moviesearcher.model.data.LocalMovieList

@Dao
interface MovieListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateMovieList(movieList: LocalMovieList): Long

    @Query("SELECT * FROM movie_list WHERE roomId = :movieListId")
    fun getMovieListById(movieListId: Int): LocalMovieList

    @Query("SELECT * FROM movie_list")
    fun getAllMovieLists(): LiveData<List<LocalMovieList>>

    @Delete
    fun deleteList(list: LocalMovieList)

    @Query("DELETE FROM movie_list")
    fun deleteAllLists()
}