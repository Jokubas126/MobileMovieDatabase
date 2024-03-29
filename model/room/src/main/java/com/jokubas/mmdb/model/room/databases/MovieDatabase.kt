package com.jokubas.mmdb.model.room.databases

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jokubas.mmdb.model.data.entities.Movie
import com.jokubas.mmdb.model.data.util.CountryListTypeConverter
import com.jokubas.mmdb.model.data.util.GenreListTypeConverter
import com.jokubas.mmdb.model.data.util.IntListTypeConverter
import com.jokubas.mmdb.model.room.dao.MovieDao

private const val DATABASE = "movie"

@Database(entities = [Movie::class], version = 7, exportSchema = false)
@TypeConverters(
    value = [
        IntListTypeConverter::class,
        CountryListTypeConverter::class,
        GenreListTypeConverter::class
    ]
)
abstract class MovieDatabase : RoomDatabase() {

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