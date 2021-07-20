package com.jokubas.mmdb.model.data.entities

import com.jokubas.mmdb.model.data.util.KEY_ENGLISH_NAME
import com.jokubas.mmdb.model.data.util.KEY_LANGUAGE_ISO_CODE
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

enum class CategoryType{
    GENRES, LANGUAGES
}

class Category(
    var type: CategoryType,
    val subcategoryList: List<Subcategory>
)

@Serializable
class Subcategory(
    @SerialName(KEY_LANGUAGE_ISO_CODE)
    val code: String,

    @SerialName(KEY_ENGLISH_NAME)
    val name: String
){
    var isChecked = false
}