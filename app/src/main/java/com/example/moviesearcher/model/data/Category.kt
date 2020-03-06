package com.example.moviesearcher.model.data

import android.os.Parcel
import android.os.Parcelable
import com.example.moviesearcher.util.KEY_LANGUAGE_ISO_CODE
import com.google.gson.annotations.SerializedName
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup

class Category(
        val name: String,
        subcategoryList: List<Subcategory>
) : ExpandableGroup<Subcategory>(name, subcategoryList)

class Subcategory() : Parcelable {

    constructor(code: String, name: String): this(){
        this.code = code
        this.name = name
    }

    @SerializedName(KEY_LANGUAGE_ISO_CODE)
    lateinit var code: String

    @SerializedName("english_name")
    lateinit var name: String

    // ------- Parcel part ----------//

    override fun writeToParcel(parcel: Parcel, flags: Int) {}

    override fun describeContents(): Int { return 0 }

    companion object CREATOR : Parcelable.Creator<Subcategory> {
        override fun createFromParcel(parcel: Parcel): Subcategory {
            return Subcategory()
        }
        override fun newArray(size: Int): Array<Subcategory?> {
            return arrayOfNulls(size)
        }
    }
}