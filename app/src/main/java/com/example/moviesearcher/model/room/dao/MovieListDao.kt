package com.example.moviesearcher.model.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.moviesearcher.model.data.MovieList

@Dao
interface MovieListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateMovieList(movieList: MovieList): Long

    @Query("SELECT * FROM movie_list WHERE uid = :movieListId")
    fun getMovieListById(movieListId: Int): MovieList

    @Query("SELECT * FROM movie_list")
    fun getAllMovieLists(): LiveData<List<MovieList>>

    @Delete
    fun deleteList(list: MovieList)

    @Query("DELETE FROM movie_list")
    fun deleteAllLists()
}