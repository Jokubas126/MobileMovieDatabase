package com.jokubas.mmdb.moviedetails.model.entities

import com.jokubas.mmdb.model.data.util.KEY_RESULT_LIST
import com.jokubas.mmdb.util.constants.KEY_TRAILER_TYPE
import com.jokubas.mmdb.util.constants.KEY_YOUTUBE_SITE
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
class VideoResults(
    @SerialName(KEY_RESULT_LIST)
    val videoList: List<Video>
) {

    @Transient
    val mainTrailer: Video? = videoList.find { video ->
        video.siteType == KEY_YOUTUBE_SITE && video.videoType == KEY_TRAILER_TYPE
    }
}