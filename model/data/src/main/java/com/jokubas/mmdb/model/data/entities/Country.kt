package com.jokubas.mmdb.model.data.entities

import com.jokubas.mmdb.model.data.util.KEY_COUNTRY_ISO_CODE
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Country(
    @SerialName(KEY_COUNTRY_ISO_CODE)
    val code: String,
    val name: String
)