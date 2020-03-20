package com.example.moviesearcher.util

import android.content.Context
import androidx.room.TypeConverter
import com.example.moviesearcher.model.data.Country
import com.example.moviesearcher.model.data.Genre
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.BufferedInputStream
import java.lang.reflect.Type
import java.net.URL
import java.nio.ByteBuffer
import java.util.*


// ---------------- Text related -------------//

fun stringListToString(list: List<String>): String {
    val stringBuilder = java.lang.StringBuilder()
    for (word in list) {
        if (stringBuilder.isNotBlank())
            stringBuilder.append(", ").append(word)
        else stringBuilder.append(word)
    }
    return stringBuilder.toString()
}

fun stringListToListedString(list: List<String>): String {
    val stringBuilder = java.lang.StringBuilder()
    for (word in list) {
        if (stringBuilder.isNotBlank())
            stringBuilder.append("\n").append(word)
        else stringBuilder.append(word)
    }
    return stringBuilder.toString()
}

fun getAnyNameList(list: List<*>?): List<String> {
    val nameList = mutableListOf<String>()
    if (list != null) {
        for (value in list)
            when (value) {
                is Genre -> nameList.add(value.name)
                is Country -> nameList.add(value.name)
            }
    }
    return nameList
}

// ---------------- Type converters -------------//

class IntListTypeConverter {
    /** based on https://medium.com/@toddcookevt/android-room-storing-lists-of-objects-766cca57e3f9 **/
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

// --------------- Image Related ---------------- //

class ImageTypeConverter {

    @TypeConverter
    fun imageUrlToByteArray(url: String): ByteArray {

        val inputStream = BufferedInputStream(URL(url).openConnection().getInputStream())

        val byteBuffer = ByteBuffer.allocateDirect(1000)

        var current = 0
        while (inputStream.read().also { current = it } != -1) {
            byteBuffer.put(current.toByte())
        }

        return byteBuffer.array()
    }
}


