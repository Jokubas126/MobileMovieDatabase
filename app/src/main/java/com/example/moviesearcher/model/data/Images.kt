package com.example.moviesearcher.model.data

import android.graphics.Bitmap
import androidx.room.*
import com.example.moviesearcher.util.*
import com.google.gson.annotations.SerializedName

@Entity(tableName = "images")
class Images(
    @PrimaryKey(autoGenerate = false)
    var movieRoomId: Int,

    @TypeConverters(ImageListTypeConverter::class)
    @ColumnInfo(name = "poster_list")
    @SerializedName(KEY_POSTER_LIST)
    val posterList: List<Image>?,

    @TypeConverters(ImageListTypeConverter::class)
    @ColumnInfo(name = "backdrop_list")
    @SerializedName(KEY_BACKDROP_LIST)
    val backdropList: List<Image>?
) {
    fun generateBitmaps() {
        if (!posterList.isNullOrEmpty())
            for (poster in posterList)
                poster.imageBitmap = imageUrlToBitmap(poster.filePath)

        if (!backdropList.isNullOrEmpty())
            for (backdrop in backdropList)
                backdrop.imageBitmap = imageUrlToBitmap(backdrop.filePath)

    }
    constructor() : this(0, emptyList(), emptyList())
}

data class Image(
    @Ignore
    @SerializedName(KEY_IMAGE_FILE_PATH)
    val filePath: String,

    @TypeConverters(BitmapTypeConverter::class)
    @ColumnInfo(name = "image_bitmap")
    var imageBitmap: Bitmap?
)
