package com.jokubas.mmdb.moviedetails.ui.media

import com.jokubas.mmdb.moviedetails.model.entities.Images
import com.jokubas.mmdb.moviedetails.model.entities.Video

data class MediaInfo(
    val images: Images? = null,
    val trailer: Video? = null
)