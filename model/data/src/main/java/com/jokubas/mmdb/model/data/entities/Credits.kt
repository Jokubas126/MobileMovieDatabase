package com.jokubas.mmdb.model.data.entities

import android.content.Context
import androidx.room.*
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import com.jokubas.mmdb.model.data.util.*
import java.lang.reflect.Type
import java.util.*

@Entity(tableName = "credits")
class Credits(

    @PrimaryKey(autoGenerate = false)
    var movieRoomId: Int,

    @TypeConverters(PersonListTypeConverter::class)
    @ColumnInfo(name = KEY_CAST_LIST)
    @SerializedName(KEY_CAST_LIST)
    val castList: List<Person>?,

    @TypeConverters(PersonListTypeConverter::class)
    @ColumnInfo(name = KEY_CREW_LIST)
    @SerializedName(KEY_CREW_LIST)
    val crewList: List<Person>?
) {
    // upload images to file system and get their URIs
    fun generateFileUris(context: Context): Credits {
        if (!castList.isNullOrEmpty())
            for (cast in castList)
                if (!cast.profileImageUrl.isNullOrBlank())
                    cast.profileImageUriString = imageUrlToFileUriString(context, cast.profileImageUrl)

        if (!crewList.isNullOrEmpty())
            for (crew in crewList)
                if (!crew.profileImageUrl.isNullOrBlank())
                    crew.profileImageUriString = imageUrlToFileUriString(context, crew.profileImageUrl)
        return this
    }
}

data class Person(
    val name: String?,

    @SerializedName(KEY_CAST_POSITION, alternate = [KEY_CREW_POSITION])
    val position: String?,

    @Ignore
    @SerializedName(KEY_PROFILE_IMAGE_URL)
    val profileImageUrl: String?,

    @ColumnInfo(name = KEY_PROFILE_IMAGE_URI_STRING)
    var profileImageUriString: String?
)

class PersonListTypeConverter {

    @TypeConverter
    fun personListToString(personList: List<Person>?): String? {
        return Gson().toJson(personList)
    }

    @TypeConverter
    fun stringToIntList(string: String?): List<Person>? {
        if (string == null)
            return Collections.emptyList()
        val listType: Type = object : TypeToken<List<Person?>?>() {}.type
        return Gson().fromJson(string, listType)
    }
}