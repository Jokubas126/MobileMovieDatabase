package com.example.moviesearcher.model.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.moviesearcher.model.data.Movie
import com.example.moviesearcher.model.room.dao.MovieDao
import com.example.moviesearcher.util.BitmapTypeConverter

private const val DATABASE = "movie"

@Database(entities = [Movie::class], version = 4, exportSchema = false)
@TypeConverters(BitmapTypeConverter::class)
abstract class MovieDatabase: RoomDatabase(){

    abstract fun movieDao(): MovieDao

    companion object {

        @Volatile
        private var instance: MovieDatabase? = null

        fun getInstance(context: Context): MovieDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): MovieDatabase {
            return Room.databaseBuilder(
                    context.applicationContext,
                    MovieDatabase::class.java,
                    DATABASE
                )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}