package com.jokubas.mmdb.model.room.databases

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jokubas.mmdb.model.data.dataclasses.WatchlistMovie
import com.jokubas.mmdb.model.room.dao.WatchlistMovieDao

private const val DATABASE = "watchlist"

@Database(entities = [WatchlistMovie::class], version = 1, exportSchema = false)
abstract class WatchlistDatabase: RoomDatabase() {

    abstract fun watchlistMovieDao(): WatchlistMovieDao

    companion object {

        @Volatile
        private var instance: WatchlistDatabase? = null

        fun getInstance(context: Context): WatchlistDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): WatchlistDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                WatchlistDatabase::class.java,
                DATABASE
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}