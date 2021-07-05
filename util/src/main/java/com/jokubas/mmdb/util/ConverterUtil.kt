package com.jokubas.mmdb.util

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.*
import java.lang.reflect.Type
import java.util.*

// --------------- Image Related ---------------- //

fun saveBitmapToFile(context: Context, bitmap: Bitmap?): File? {
    return bitmap?.let {
        val wrapper = ContextWrapper(context)
        var file = wrapper.getDir("images", Context.MODE_PRIVATE)
        file = File(file, "${UUID.randomUUID()}.jpg")
        val stream: OutputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        stream.flush()
        stream.close()
        file
    }
}

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