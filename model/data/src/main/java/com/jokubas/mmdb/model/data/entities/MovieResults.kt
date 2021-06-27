package com.jokubas.mmdb.model.data.entities

import com.google.gson.annotations.SerializedName
import com.jokubas.mmdb.model.data.util.KEY_RESULT_LIST
import com.jokubas.mmdb.model.data.util.KEY_TOTAL_PAGES

class MovieResults(
    @SerializedName("page")
    val page: Int,

    @SerializedName(KEY_RESULT_LIST)
    val movieList: List<Movie>,

    @SerializedName(KEY_TOTAL_PAGES)
    val totalPages: Int
)