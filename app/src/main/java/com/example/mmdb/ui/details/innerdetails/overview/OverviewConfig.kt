package com.example.mmdb.ui.details.innerdetails.overview

import com.jokubas.mmdb.model.data.entities.Movie
import com.jokubas.mmdb.util.DataResponse
import kotlinx.coroutines.flow.Flow

class OverviewConfig(
    val provideOverviewInfo: suspend (movieId: Int, isRemote: Boolean) -> Flow<DataResponse<Movie>>
)