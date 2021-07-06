package com.jokubas.mmdb.model.data.entities

import android.content.Context
import androidx.room.*
import com.google.gson.annotations.SerializedName
import com.jokubas.mmdb.model.data.util.*
import com.jokubas.mmdb.util.extensions.urlToFileUriString
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "images")
class Images(
    @PrimaryKey(autoGenerate = false)
    var movieRoomId: Int,

    @TypeConverters(ImageListTypeConverter::class)
    @ColumnInfo(name = KEY_LOCAL_POSTER_LIST)
    @SerialName(KEY_POSTER_LIST)
    val posterList: List<Image>?,

    @TypeConverters(ImageListTypeConverter::class)
    @ColumnInfo(name = KEY_LOCAL_BACKDROP_LIST)
    @SerialName(KEY_BACKDROP_LIST)
    val backdropList: List<Image>?
) {
    fun generateFileUris(context: Context): Images {
        if (!posterList.isNullOrEmpty())
            for (poster in posterList)
                poster.imageUriString = context.urlToFileUriString(poster.imageUrl)

        if (!backdropList.isNullOrEmpty())
            for (backdrop in backdropList)
                backdrop.imageUriString = context.urlToFileUriString(backdrop.imageUrl)

        return this
    }
    constructor() : this(
        movieRoomId = 0,
        posterList = emptyList(),
        backdropList = emptyList()
    )
}

@Serializable
data class Image(
    @Ignore
    @SerialName(KEY_IMAGE_URL)
    val imageUrl: String?,

    @ColumnInfo(name = KEY_IMAGE_URI_STRING)
    var imageUriString: String?
)

