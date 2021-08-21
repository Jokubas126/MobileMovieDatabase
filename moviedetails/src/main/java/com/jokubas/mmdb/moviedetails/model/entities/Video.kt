package com.jokubas.mmdb.moviedetails.model.entities

import com.jokubas.mmdb.model.data.util.KEY_VIDEO_SITE
import com.jokubas.mmdb.model.data.util.KEY_VIDEO_TYPE
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Video(
    val key: String,
    val name: String,

    @SerialName(KEY_VIDEO_SITE)
    val siteType: String,

    @SerialName(KEY_VIDEO_TYPE)
    val videoType: String
)
