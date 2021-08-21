package com.jokubas.mmdb.moviedetails.model.room.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jokubas.mmdb.moviedetails.model.entities.Person
import java.lang.reflect.Type
import java.util.*

class PersonListTypeConverter {

    @TypeConverter
    fun personListToString(personList: List<Person>?): String? {
        return Gson().toJson(personList)
    }

    @TypeConverter
    fun stringToIntList(string: String?): List<Person>? {
        if (string == null)
            return Collections.emptyList()
        val listType: Type = object : TypeToken<List<Person?>?>() {}.type
        return Gson().fromJson(string, listType)
    }
}