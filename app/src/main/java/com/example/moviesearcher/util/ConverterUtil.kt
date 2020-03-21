package com.example.moviesearcher.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64.*
import androidx.room.TypeConverter
import com.example.moviesearcher.model.data.Country
import com.example.moviesearcher.model.data.Genre
import com.example.moviesearcher.model.data.Image
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.BufferedInputStream
import java.io.ByteArrayOutputStream
import java.lang.reflect.Type
import java.net.URL
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

// --------------- Image Related ---------------- //

fun imageUrlToBitmap(url: String): Bitmap {
    val inputStream =
        BufferedInputStream(URL(BASE_IMAGE_URL + url).openConnection().getInputStream())
    val byteArray = inputStream.readBytes()
    return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
}

// ---------------------------------------------------------//
    // ---------------- Type converters ---------------//
// ---------------------------------------------------------//

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

class BitmapTypeConverter {

    @TypeConverter
    fun bitmapToString(bitmap: Bitmap): String? {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        return encodeToString(stream.toByteArray(), DEFAULT)
    }

    @TypeConverter
    fun stringToBitmap(string: String?): Bitmap? {
        val imageBytes = decode(string, 0)
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }
}

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


