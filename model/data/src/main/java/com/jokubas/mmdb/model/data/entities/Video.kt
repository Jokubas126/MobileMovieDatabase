package com.jokubas.mmdb.model.data.entities

import com.google.gson.annotations.SerializedName
import com.jokubas.mmdb.model.data.util.KEY_RESULT_LIST
import com.jokubas.mmdb.model.data.util.KEY_VIDEO_SITE
import com.jokubas.mmdb.model.data.util.KEY_VIDEO_TYPE
import com.jokubas.mmdb.util.constants.KEY_TRAILER_TYPE
import com.jokubas.mmdb.util.constants.KEY_YOUTUBE_SITE

class VideoResults(
    @SerializedName(KEY_RESULT_LIST)
    val videoList: List<Video>
)

data class Video(
    val key: String,
    val name: String,

    @SerializedName(KEY_VIDEO_SITE)
    val siteType: String,

    @SerializedName(KEY_VIDEO_TYPE)
    val videoType: String
)

fun VideoResults.filterMainTrailer(): Video? = videoList.firstOrNull { video ->
    video.siteType == KEY_YOUTUBE_SITE && video.videoType == KEY_TRAILER_TYPE
}
