package com.example.mmdb.ui.details.overview

import com.example.mmdb.ui.details.DetailsMovieId
import com.jokubas.mmdb.util.DataResponse

class OverviewConfig(
    val provideOverviewInfo: suspend (movieId: DetailsMovieId) -> DataResponse
)