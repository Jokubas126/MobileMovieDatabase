package com.jokubas.mmdb.moviedetails.model.entities

import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.jokubas.mmdb.moviedetails.model.room.converters.PersonListTypeConverter
import com.jokubas.mmdb.util.extensions.imageUrlToFileUriString
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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


