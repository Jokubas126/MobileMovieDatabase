package com.example.moviesearcher.model.data

import com.example.moviesearcher.util.*
import com.google.gson.annotations.SerializedName

class Credits (
        @SerializedName(KEY_CAST_LIST)
        val castList: List<Person>,
        @SerializedName(KEY_CREW_LIST)
        val crewList: List<Person>
)

data class Person(
        val name: String,

        @SerializedName(KEY_CAST_POSITION, alternate = [KEY_CREW_POSITION])
        val position: String,

        @SerializedName(KEY_PROFILE_IMAGE_PATH)
        val profileImagePath: String
)