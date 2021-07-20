package com.jokubas.mmdb.model.data.entities

import android.content.Context
import androidx.room.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jokubas.mmdb.model.data.util.*
import com.jokubas.mmdb.util.extensions.imageUrlToFileUriString
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNames
import java.lang.reflect.Type
import java.util.*

@Serializable
@Entity(tableName = "credits")
class Credits(

    @SerialName("id")
    @PrimaryKey(autoGenerate = false)
    val id: Int,

    @TypeConverters(PersonListTypeConverter::class)
    @ColumnInfo(name = "cast")
    @SerialName("cast")
    val castList: List<Person> = emptyList(),

    @TypeConverters(PersonListTypeConverter::class)
    @ColumnInfo(name = "crew")
    @SerialName("crew")
    val crewList: List<Person> = emptyList()
) {

    // upload images to file system and get their URIs
    fun generateFileUris(context: Context) {
        castList.forEach { person ->
            person.profileImageUriString = context.imageUrlToFileUriString(person.profileImageUrl)
                .takeUnless { person.profileImageUrl.isNullOrBlank() }
        }
        crewList.forEach { person ->
            person.profileImageUriString = context.imageUrlToFileUriString(person.profileImageUrl)
                .takeUnless { person.profileImageUrl.isNullOrBlank() }
        }
    }
}

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

@Serializable
data class Person(
    @ColumnInfo(name = "name")
    @SerialName("name")
    val name: String,

    @ColumnInfo(name = "position")
    @JsonNames(*["job"])
    @SerialName("character")
    val position: String,

    @ColumnInfo(name = "profile_image_uri_path")
    @SerialName("profile_path")
    val profileImageUrl: String? = null
) {
    @Transient
    @ColumnInfo(name = "profile_image_uri_string")
    var profileImageUriString: String? = null
}

//val personPosition = Json.decodeFromString<Person>("""{"name":"kotlinx.serialization"}""")
