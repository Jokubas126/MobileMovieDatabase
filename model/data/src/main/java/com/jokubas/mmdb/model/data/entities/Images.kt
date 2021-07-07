package com.jokubas.mmdb.model.data.entities

import android.content.Context
import androidx.room.*
import com.jokubas.mmdb.model.data.util.*
import com.jokubas.mmdb.util.extensions.imageUrlToFileUriString
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "images")
class Images(
    @PrimaryKey(autoGenerate = false)
    var movieRoomId: Int = 0,

    @TypeConverters(ImageListTypeConverter::class)
    @ColumnInfo(name = KEY_LOCAL_POSTER_LIST)
    @SerialName(KEY_POSTER_LIST)
    val posterList: List<Image> = emptyList(),

    @TypeConverters(ImageListTypeConverter::class)
    @ColumnInfo(name = KEY_LOCAL_BACKDROP_LIST)
    @SerialName(KEY_BACKDROP_LIST)
    val backdropList: List<Image> = emptyList()
) {
    fun generateFileUris(context: Context): Images {
        if (!posterList.isNullOrEmpty())
            for (poster in posterList)
                poster.imageUriString = context.imageUrlToFileUriString(poster.imageUrl)

        if (!backdropList.isNullOrEmpty())
            for (backdrop in backdropList)
                backdrop.imageUriString = context.imageUrlToFileUriString(backdrop.imageUrl)

        return this
    }
}

@Serializable
data class Image(
    @Ignore
    @SerialName(KEY_IMAGE_URL)
    val imageUrl: String?,

    @ColumnInfo(name = KEY_IMAGE_URI_STRING)
    var imageUriString: String?
)

