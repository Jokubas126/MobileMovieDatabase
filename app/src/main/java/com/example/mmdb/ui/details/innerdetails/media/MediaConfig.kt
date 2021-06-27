package com.example.mmdb.ui.details.innerdetails.media

import com.jokubas.mmdb.util.DataResponse

class MediaConfig(
    val provideMediaInfo: suspend (movieId: Int, isRemote: Boolean) -> DataResponse
)