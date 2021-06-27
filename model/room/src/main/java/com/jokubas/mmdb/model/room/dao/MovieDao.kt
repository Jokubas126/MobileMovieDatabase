package com.jokubas.mmdb.model.room.dao

import androidx.room.*
import com.jokubas.mmdb.model.data.entities.Movie

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateMovie(movie: Movie): Long

    @Query("SELECT * FROM movie WHERE id = :movieId")
    suspend fun getMovieById(movieId: Int): Movie?

    @Query("SELECT * FROM movie WHERE id IN (:movieIdList)")
    suspend fun getMoviesFromIdList(movieIdList: List<Int>): List<Movie>

    @Query("DELETE FROM movie WHERE id = :movieId")
    fun deleteMovieById(movieId: Int)

    @Delete
    fun deleteMovie(movie: Movie)
}