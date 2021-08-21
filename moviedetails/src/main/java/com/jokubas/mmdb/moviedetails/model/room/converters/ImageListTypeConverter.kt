package com.jokubas.mmdb.moviedetails.model.room.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jokubas.mmdb.moviedetails.model.entities.Image
import java.lang.reflect.Type
import java.util.*

class ImageListTypeConverter {

    @TypeConverter
    fun imageListToString(imageList: List<Image>?): String? {
        return Gson().toJson(imageList)
    }

    @TypeConverter
    fun stringToImageList(string: String?): List<Image>? {
        if (string == null)
            return Collections.emptyList()
        val listType: Type = object : TypeToken<List<Image?>?>() {}.type
        return Gson().fromJson(string, listType)
    }
}