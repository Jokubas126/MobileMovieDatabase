package com.example.moviesearcher.model.data

import android.graphics.Bitmap
import androidx.room.*
import com.example.moviesearcher.util.*
import com.google.gson.annotations.SerializedName

@Entity(tableName = "credits")
class Credits(

    @PrimaryKey(autoGenerate = false)
    var movieRoomId: Int,

    @TypeConverters(PersonListTypeConverter::class)
    @ColumnInfo(name = "cast")
    @SerializedName(KEY_CAST_LIST)
    val castList: List<Person>,

    @TypeConverters(PersonListTypeConverter::class)
    @ColumnInfo(name = "crew")
    @SerializedName(KEY_CREW_LIST)
    val crewList: List<Person>
) {
    fun generateBitmaps(): Credits {
        for (cast in castList)
            if (!cast.profileImagePath.isNullOrBlank())
                cast.profileImageBitmap = imageUrlToBitmap(cast.profileImagePath)
        for (crew in crewList)
            if (!crew.profileImagePath.isNullOrBlank())
                crew.profileImageBitmap = imageUrlToBitmap(crew.profileImagePath)
        return this
    }
}

data class Person(
    val name: String,

    @SerializedName(KEY_CAST_POSITION, alternate = [KEY_CREW_POSITION])
    val position: String,

    @Ignore
    @SerializedName(KEY_PROFILE_IMAGE_PATH)
    val profileImagePath: String?,

    @TypeConverters(BitmapTypeConverter::class)
    @ColumnInfo(name = "profile_image_bitmap")
    var profileImageBitmap: Bitmap?
)