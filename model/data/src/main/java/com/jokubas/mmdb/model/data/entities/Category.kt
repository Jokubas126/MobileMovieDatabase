package com.jokubas.mmdb.model.data.entities

import com.google.gson.annotations.SerializedName
import com.jokubas.mmdb.model.data.util.KEY_ENGLISH_NAME
import com.jokubas.mmdb.model.data.util.KEY_LANGUAGE_ISO_CODE
import kotlinx.serialization.SerialName

enum class CategoryType{
    GENRES, LANGUAGES
}

class Category(
    var type: CategoryType,
    val subcategoryList: List<Subcategory>
)

class Subcategory(
    @SerialName(KEY_LANGUAGE_ISO_CODE)
    val code: String,

    @SerialName(KEY_ENGLISH_NAME)
    val name: String
){
    var isChecked = false
}