package com.jokubas.mmdb.model.data.entities

import com.jokubas.mmdb.model.data.util.KEY_RESULT_LIST
import com.jokubas.mmdb.model.data.util.KEY_TOTAL_PAGES
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class MovieResults(
    @SerialName("page")
    val page: Int,

    @SerialName(KEY_RESULT_LIST)
    val movieList: List<MovieSummary>,

    @SerialName(KEY_TOTAL_PAGES)
    val totalPages: Int
)