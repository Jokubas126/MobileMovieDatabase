package com.jokubas.mmdb.moviedetails.model.entities

import androidx.room.ColumnInfo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.JsonNames

@Serializable
data class Person(
    @ColumnInfo(name = "name")
    @SerialName("name")
    val name: String,

    @ColumnInfo(name = "position")
    @JsonNames(*["job"])
    @SerialName("character")
    val position: String,

    @ColumnInfo(name = "profile_image_uri_path")
    @SerialName("profile_path")
    val profileImageUrl: String? = null
) {
    @Transient
    @ColumnInfo(name = "profile_image_uri_string")
    var profileImageUriString: String? = null
}