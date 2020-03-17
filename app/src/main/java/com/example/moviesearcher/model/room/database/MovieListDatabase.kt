package com.example.moviesearcher.model.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.moviesearcher.model.data.MovieList
import com.example.moviesearcher.model.room.dao.MovieListDao

private const val DATABASE = "movie_list"

@Database(entities = [MovieList::class], version = 1, exportSchema = false)
abstract class MovieListDatabase : RoomDatabase() {

    abstract fun movieListDao(): MovieListDao

    companion object {

        @Volatile
        private var instance: MovieListDatabase? = null

        fun getInstance(context: Context): MovieListDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): MovieListDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                MovieListDatabase::class.java,
                DATABASE
            ).build()
        }
    }
}