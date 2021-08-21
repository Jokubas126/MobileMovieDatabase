package com.jokubas.mmdb.moviedetails.model.local.databases

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jokubas.mmdb.moviedetails.model.local.dao.ImagesDao
import com.jokubas.mmdb.moviedetails.model.converters.ImageListTypeConverter
import com.jokubas.mmdb.moviedetails.model.entities.Images

private const val DATABASE = "images"

@Database(entities = [Images::class], version = 7, exportSchema = false)
@TypeConverters(ImageListTypeConverter::class)
abstract class ImagesDatabase : RoomDatabase() {

    abstract fun imagesDao(): ImagesDao

    companion object {

        @Volatile
        private var instance: ImagesDatabase? = null

        fun getInstance(context: Context): ImagesDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): ImagesDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                ImagesDatabase::class.java,
                DATABASE
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}