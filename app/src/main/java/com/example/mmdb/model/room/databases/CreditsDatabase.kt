package com.example.mmdb.model.room.databases

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.mmdb.model.data.Credits
import com.example.mmdb.model.room.dao.CreditsDao
import com.example.mmdb.util.PersonListTypeConverter

private const val DATABASE = "credits"


@Database(entities = [Credits::class], version = 5, exportSchema = false)
@TypeConverters(PersonListTypeConverter::class)
abstract class CreditsDatabase: RoomDatabase(){

    abstract fun creditsDao(): CreditsDao

    companion object {

        @Volatile
        private var instance: CreditsDatabase? = null

        fun getInstance(context: Context): CreditsDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): CreditsDatabase {
            return Room.databaseBuilder(
                    context.applicationContext,
                    CreditsDatabase::class.java,
                    DATABASE
                )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}