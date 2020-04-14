package com.example.mmdb.model.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.mmdb.model.data.CustomMovieList

@Dao
interface MovieListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateCustomList(movieList: CustomMovieList): Long

    @Query("SELECT * FROM movie_list WHERE roomId = :customListId")
    fun getCustomListById(customListId: Int): CustomMovieList

    @Query("SELECT * FROM movie_list")
    fun getAllCustomMovieLists(): LiveData<List<CustomMovieList>>

    @Delete
    fun deleteCustomList(list: CustomMovieList)

    @Query("DELETE FROM movie_list")
    fun deleteAllCustomLists()
}