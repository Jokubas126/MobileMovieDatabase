package com.example.mmdb.model.data

import android.os.Parcel
import android.os.Parcelable
import com.example.mmdb.util.KEY_ENGLISH_NAME
import com.example.mmdb.util.KEY_LANGUAGE_ISO_CODE
import com.google.gson.annotations.SerializedName
import com.thoughtbot.expandablecheckrecyclerview.models.SingleCheckExpandableGroup

class Category(
    val name: String,
    subcategoryList: List<Subcategory>
) : SingleCheckExpandableGroup(name, subcategoryList)

class Subcategory() : Parcelable {

    constructor(code: String, name: String) : this() {
        this.code = code
        this.name = name
    }

    @SerializedName(KEY_LANGUAGE_ISO_CODE)
    lateinit var code: String

    @SerializedName(KEY_ENGLISH_NAME)
    lateinit var name: String

    // ------- Parcel part ----------//

    override fun writeToParcel(parcel: Parcel, flags: Int) {}

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Subcategory> {
        override fun createFromParcel(parcel: Parcel): Subcategory {
            return Subcategory()
        }

        override fun newArray(size: Int): Array<Subcategory?> {
            return arrayOfNulls(size)
        }
    }
}