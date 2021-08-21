package com.jokubas.mmdb.moviedetails.ui.credits

import com.jokubas.mmdb.moviedetails.model.entities.Credits
import com.jokubas.mmdb.util.DataResponse
import kotlinx.coroutines.flow.Flow

data class CreditsConfig (
    val provideCreditsDataFlow: suspend (isRemote: Boolean, movieId: Int) -> Flow<DataResponse<Credits>>
)