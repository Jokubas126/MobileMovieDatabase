package com.example.mmdb.ui.details.innerdetails.credits

import com.jokubas.mmdb.model.data.entities.Credits
import com.jokubas.mmdb.util.DataResponse
import kotlinx.coroutines.flow.Flow

data class CreditsConfig (
    val provideCreditsDataFlow: suspend (isRemote: Boolean, movieId: Int) -> Flow<DataResponse<Credits?>>
)