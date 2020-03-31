package com.example.moviesearcher.model.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.moviesearcher.model.data.Images
import com.example.moviesearcher.model.room.dao.ImagesDao
import com.example.moviesearcher.util.ImageListTypeConverter
import com.example.moviesearcher.util.BitmapTypeConverter

private const val DATABASE = "images"

@Database(entities = [Images::class], version = 3, exportSchema = false)
@TypeConverters(ImageListTypeConverter::class, BitmapTypeConverter::class)
abstract class ImagesDatabase: RoomDatabase(){

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