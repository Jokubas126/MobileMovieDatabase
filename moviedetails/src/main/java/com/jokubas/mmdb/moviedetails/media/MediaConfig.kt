package com.jokubas.mmdb.moviedetails.media

import androidx.lifecycle.Lifecycle
import com.jokubas.mmdb.model.data.entities.Images
import com.jokubas.mmdb.model.data.entities.VideoResults
import com.jokubas.mmdb.util.DataResponse
import kotlinx.coroutines.flow.Flow

class MediaConfig(
    val lifecycle: Lifecycle,
    val provideImages: suspend (movieId: Int, isRemote: Boolean) -> Flow<DataResponse<Images>>,
    val provideTrailer: suspend (movieId: Int, isRemote: Boolean) -> Flow<DataResponse<VideoResults>>
)