package com.example.mmdb.ui.details.innerdetails.overview

import com.jokubas.mmdb.util.DataResponse

class OverviewConfig(
    val provideOverviewInfo: suspend (movieId: Int, isRemote: Boolean) -> DataResponse
)