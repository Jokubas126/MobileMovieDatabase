package com.example.moviesearcher.util

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64.*
import androidx.room.TypeConverter
import com.example.moviesearcher.model.data.Country
import com.example.moviesearcher.model.data.Genre
import com.example.moviesearcher.model.data.Image
import com.example.moviesearcher.model.data.Person
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.*
import java.lang.reflect.Type
import java.net.HttpURLConnection
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

fun imageUrlToFileUriString(context: Context, url: String?): String? {
    if (!url.isNullOrBlank()){
        val connection: HttpURLConnection = URL(BASE_IMAGE_URL + url).openConnection() as HttpURLConnection
        connection.connect()
        val inputStream = connection.inputStream
        val bitmap = BitmapFactory.decodeStream(inputStream)
        val file = saveBitmapToFile(context, bitmap)
        return Uri.parse(file?.absolutePath).toString()
    }
    return null
}

private fun saveBitmapToFile(context: Context, bitmap: Bitmap?): File? {
    if (bitmap != null){
        val wrapper = ContextWrapper(context)
        var file = wrapper.getDir("images", Context.MODE_PRIVATE)
        file = File(file, "${UUID.randomUUID()}.jpg")
        val stream: OutputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        stream.flush()
        stream.close()
        return file
    }
    return null
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

// --------------- Image Related ---------------- //

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


