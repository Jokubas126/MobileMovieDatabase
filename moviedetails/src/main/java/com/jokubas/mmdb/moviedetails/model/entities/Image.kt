package com.jokubas.mmdb.moviedetails.model.entities

import androidx.room.ColumnInfo
import androidx.room.Ignore
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

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