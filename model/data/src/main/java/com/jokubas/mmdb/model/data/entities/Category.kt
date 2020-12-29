package com.jokubas.mmdb.model.data.entities

import com.google.gson.annotations.SerializedName
import com.jokubas.mmdb.model.data.util.KEY_ENGLISH_NAME
import com.jokubas.mmdb.model.data.util.KEY_LANGUAGE_ISO_CODE

class Category(
    val name: String,
    val subcategoryList: List<Subcategory>
)

class Subcategory(
    @SerializedName(KEY_LANGUAGE_ISO_CODE)
    val code: String,

    @SerializedName(KEY_ENGLISH_NAME)
    val name: String
)