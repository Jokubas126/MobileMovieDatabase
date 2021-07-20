package com.example.mmdb.ui.details.innerdetails.media

import com.jokubas.mmdb.model.data.entities.Images
import com.jokubas.mmdb.model.data.entities.VideoResults
import com.jokubas.mmdb.util.DataResponse
import kotlinx.coroutines.flow.Flow

class MediaConfig(
    val provideImages: suspend (movieId: Int, isRemote: Boolean) -> Flow<DataResponse<Images?>>,
    val provideTrailer: suspend (movieId: Int, isRemote: Boolean) -> Flow<DataResponse<VideoResults>>
)