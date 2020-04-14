package com.example.mmdb.model.data

import android.content.Context
import androidx.room.*
import com.example.mmdb.util.*
import com.google.gson.annotations.SerializedName

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
