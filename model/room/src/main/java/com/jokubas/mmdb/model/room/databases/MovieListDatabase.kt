package com.jokubas.mmdb.model.room.databases

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jokubas.mmdb.model.data.entities.CustomMovieList
import com.jokubas.mmdb.model.room.dao.MovieListDao
import com.jokubas.mmdb.model.data.util.DateConverter
import com.jokubas.mmdb.model.data.util.IntListTypeConverter

private const val DATABASE = "movie_list"

@Database(entities = [CustomMovieList::class], version = 5, exportSchema = false)
@TypeConverters(IntListTypeConverter::class, DateConverter::class)
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