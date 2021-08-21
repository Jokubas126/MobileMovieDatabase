package com.jokubas.mmdb.moviedetails.media

import com.jokubas.mmdb.model.data.entities.Images
import com.jokubas.mmdb.model.data.entities.Video

data class MediaInfo(
    val images: Images? = null,
    val trailer: Video? = null
)