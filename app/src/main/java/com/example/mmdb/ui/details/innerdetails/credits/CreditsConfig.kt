package com.example.mmdb.ui.details.innerdetails.credits

import com.jokubas.mmdb.util.DataResponse
import kotlinx.coroutines.flow.Flow

data class CreditsConfig (
    val provideCreditsDataFlow: (isRemote: Boolean, movieId: Int) -> Flow<DataResponse>
)