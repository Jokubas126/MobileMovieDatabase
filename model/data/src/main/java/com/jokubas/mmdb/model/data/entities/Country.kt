package com.jokubas.mmdb.model.data.entities

import com.google.gson.annotations.SerializedName
import com.jokubas.mmdb.model.data.util.KEY_COUNTRY_ISO_CODE

data class Country(
    @SerializedName(KEY_COUNTRY_ISO_CODE)
    val code: String,
    val name: String
)