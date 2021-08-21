package com.jokubas.mmdb.model.data.entities

import android.os.Parcelable
import com.jokubas.mmdb.model.data.util.KEY_LANGUAGE_ISO_CODE
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

enum class CategoryType {
    GENRES, LANGUAGES
}

class Category(
    var type: CategoryType,
    val subcategoryList: List<Subcategory>
)

@Parcelize
@Serializable
class Subcategory(
    @SerialName(KEY_LANGUAGE_ISO_CODE)
    val code: String,

    @SerialName("english_name")
    val name: String
) : Parcelable {

    @IgnoredOnParcel
    @Transient
    var isChecked: Boolean = false
}