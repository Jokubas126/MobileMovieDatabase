package com.jokubas.mmdb.model.room.dao

import androidx.room.*
import com.jokubas.mmdb.model.data.entities.CustomMovieList
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateCustomList(movieList: CustomMovieList): Long

    @Query("SELECT * FROM movie_list WHERE roomId = :customListId")
    fun getCustomListById(customListId: Int): CustomMovieList

    @Query("SELECT * FROM movie_list")
    fun getAllCustomMovieListFlow(): Flow<List<CustomMovieList>>

    @Query("SELECT * FROM movie_list")
    suspend fun getAllCustomMovieLists(): List<CustomMovieList>

    @Delete
    fun deleteCustomList(list: CustomMovieList)
}