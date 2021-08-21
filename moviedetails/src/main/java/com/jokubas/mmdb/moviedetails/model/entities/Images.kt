package com.jokubas.mmdb.moviedetails.model.entities

import android.content.Context
import androidx.room.*
import com.jokubas.mmdb.moviedetails.model.converters.ImageListTypeConverter
import com.jokubas.mmdb.util.extensions.imageUrlToFileUriString
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
@Entity(tableName = "images")
class Images(
    @SerialName("id")
    @PrimaryKey(autoGenerate = false)
    val id: Int,

    @TypeConverters(ImageListTypeConverter::class)
    @ColumnInfo(name = "posters")
    @SerialName("posters")
    val posterList: List<Image> = emptyList(),

    @TypeConverters(ImageListTypeConverter::class)
    @ColumnInfo(name = "backdrops")
    @SerialName("backdrops")
    val backdropList: List<Image> = emptyList()
) {
    fun generateFileUris(context: Context) {
        posterList.forEach {
            it.imageUriString = context.imageUrlToFileUriString(it.imageUrl)
        }
        backdropList.forEach {
            it.imageUriString = context.imageUrlToFileUriString(it.imageUrl)
        }
    }
}

@Serializable
data class Image(
    @Ignore
    @SerialName("file_path")
    val imageUrl: String?
) {
    @Transient
    @ColumnInfo(name = "image_uri_string")
    var imageUriString: String? = null
}
