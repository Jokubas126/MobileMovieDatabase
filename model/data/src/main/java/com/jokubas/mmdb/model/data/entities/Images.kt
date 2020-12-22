package com.jokubas.mmdb.model.data.entities

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.room.*
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import com.jokubas.mmdb.model.data.util.*
import com.jokubas.mmdb.util.saveBitmapToFile
import java.lang.reflect.Type
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

const val BASE_IMAGE_URL = "https://image.tmdb.org/t/p/w780"

@Entity(tableName = "images")
class Images(
    @PrimaryKey(autoGenerate = false)
    var movieRoomId: Int,

    @TypeConverters(ImageListTypeConverter::class)
    @ColumnInfo(name = KEY_LOCAL_POSTER_LIST)
    @SerializedName(KEY_POSTER_LIST)
    val posterList: List<Image>?,

    @TypeConverters(ImageListTypeConverter::class)
    @ColumnInfo(name = KEY_LOCAL_BACKDROP_LIST)
    @SerializedName(KEY_BACKDROP_LIST)
    val backdropList: List<Image>?
) {
    fun generateFileUris(context: Context): Images {
        if (!posterList.isNullOrEmpty())
            for (poster in posterList)
                poster.imageUriString = imageUrlToFileUriString(context, poster.imageUrl)

        if (!backdropList.isNullOrEmpty())
            for (backdrop in backdropList)
                backdrop.imageUriString = imageUrlToFileUriString(context, backdrop.imageUrl)

        return this
    }
    constructor() : this(0, emptyList(), emptyList())
}

data class Image(
    @Ignore
    @SerializedName(KEY_IMAGE_URL)
    val imageUrl: String?,

    @ColumnInfo(name = KEY_IMAGE_URI_STRING)
    var imageUriString: String?
)

fun imageUrlToFileUriString(context: Context, url: String?): String? {
    return if (!url.isNullOrBlank()) {
        try {
            val connection: HttpURLConnection =
                URL(BASE_IMAGE_URL + url).openConnection() as HttpURLConnection
            connection.connect()
            val inputStream = connection.inputStream
            val bitmap = BitmapFactory.decodeStream(inputStream)
            val file = saveBitmapToFile(context, bitmap)
            Uri.parse(file?.absolutePath).toString()
        } catch (e: Exception) {
            null
        }
    } else
        null
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