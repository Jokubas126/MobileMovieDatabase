package com.example.mmdb.model.data

import com.example.mmdb.util.KEY_COUNTRY_ISO_CODE
import com.google.gson.annotations.SerializedName

data class Country(
    @SerializedName(KEY_COUNTRY_ISO_CODE)
    val code: String,
    val name: String
)