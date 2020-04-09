package com.example.moviesearcher.model.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.moviesearcher.model.data.LocalMovieList
import com.example.moviesearcher.model.room.dao.MovieListDao
import com.example.moviesearcher.util.IntListTypeConverter


private const val DATABASE = "movie_list"

@Database(entities = [LocalMovieList::class], version = 3, exportSchema = false)
@TypeConverters(IntListTypeConverter::class)
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
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}