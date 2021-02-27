package com.example.mmdb.ui.details.innerdetails.credits

import com.example.mmdb.ui.details.IdWrapper
import com.jokubas.mmdb.util.DataResponse
import kotlinx.coroutines.flow.Flow

data class CreditsConfig (
    val provideCreditsDataFlow: (idWrapper: IdWrapper) -> Flow<DataResponse>
)