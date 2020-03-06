package com.example.moviesearcher.model.data

import com.google.gson.annotations.SerializedName

class Credits (
        @SerializedName("cast")
        val castList: List<Person>,
        @SerializedName("crew")
        val crewList: List<Person>
)

data class Person(
        val name: String,

        @SerializedName("character", alternate = ["job"])
        val position: String,

        @SerializedName("profile_path")
        val profileImagePath: String
)