package com.example.mmdb.ui.details.innerdetails.overview

import com.example.mmdb.ui.details.IdWrapper
import com.jokubas.mmdb.util.DataResponse

class OverviewConfig(
    val provideOverviewInfo: suspend (movieIdWrapper: IdWrapper) -> DataResponse
)