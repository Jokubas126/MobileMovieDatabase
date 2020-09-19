package com.jokubas.mmdb.model.data.dataclasses

import com.google.gson.annotations.SerializedName
import com.jokubas.mmdb.model.data.util.KEY_RESULT_LIST
import com.jokubas.mmdb.model.data.util.KEY_VIDEO_SITE
import com.jokubas.mmdb.model.data.util.KEY_VIDEO_TYPE

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