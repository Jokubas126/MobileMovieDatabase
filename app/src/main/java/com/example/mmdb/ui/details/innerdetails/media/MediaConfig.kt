package com.example.mmdb.ui.details.innerdetails.media

import com.example.mmdb.ui.details.IdWrapper
import com.jokubas.mmdb.util.DataResponse

class MediaConfig(
    val provideMediaInfo: suspend (idWrapper: IdWrapper) -> DataResponse
)