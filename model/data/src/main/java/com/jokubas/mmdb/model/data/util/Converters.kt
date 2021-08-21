package com.jokubas.mmdb.model.data.util

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jokubas.mmdb.model.data.entities.Country
import com.jokubas.mmdb.model.data.entities.Genre
import java.lang.reflect.Type
import java.util.*

// ---------------------------------------------------------//
// ---------------- Type converters ---------------//
// ---------------------------------------------------------//

class DateConverter {

    @TypeConverter
    fun dateToString(date: Date?): String? {
        return Gson().toJson(date)
    }

    @TypeConverter
    fun stringToDate(string: String?): Date? {
        val type: Type = object : TypeToken<Date>() {}.type
        return Gson().fromJson(string, type)
    }
}

// -------------------- Lists ---------------------------//

class IntListTypeConverter {

    @TypeConverter
    fun intListToString(intList: List<Int>?): String? {
        return Gson().toJson(intList)
    }

    @TypeConverter
    fun stringToIntList(string: String?): List<Int>? {
        if (string == null)
            return Collections.emptyList()
        val listType: Type = object : TypeToken<List<Int?>?>() {}.type
        return Gson().fromJson(string, listType)
    }
}

class CountryListTypeConverter {

    @TypeConverter
    fun countryListToString(countryList: List<Country>): String? {
        return Gson().toJson(countryList)
    }

    @TypeConverter
    fun stringToCountryList(string: String?): List<Country>? {
        if (string == null)
            return Collections.emptyList()
        val listType: Type = object : TypeToken<List<Country?>?>() {}.type
        return Gson().fromJson(string, listType)
    }
}

class GenreListTypeConverter {

    @TypeConverter
    fun genreListToString(genreList: List<Genre>): String? {
        return Gson().toJson(genreList)
    }

    @TypeConverter
    fun stringToGenreList(string: String?): List<Genre>? {
        if (string == null)
            return Collections.emptyList()
        val listType: Type = object : TypeToken<List<Genre?>?>() {}.type
        return Gson().fromJson(string, listType)
    }
}