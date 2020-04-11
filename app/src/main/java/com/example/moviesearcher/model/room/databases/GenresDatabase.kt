package com.example.moviesearcher.model.room.databases

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.moviesearcher.model.data.Genre
import com.example.moviesearcher.model.room.dao.GenresDao

private const val DATABASE = "genres"

@Database(entities = [Genre::class], version = 1, exportSchema = false)
abstract class GenresDatabase: RoomDatabase() {

    abstract fun genresDao(): GenresDao

    companion object {

        @Volatile
        private var instance: GenresDatabase? = null

        fun getInstance(context: Context): GenresDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): GenresDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                GenresDatabase::class.java,
                DATABASE
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}