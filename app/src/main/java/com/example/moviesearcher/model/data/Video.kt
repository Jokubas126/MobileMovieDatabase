package com.example.moviesearcher.model.data

import com.google.gson.annotations.SerializedName

class VideoResults(
        @SerializedName("results")
        val videoList: List<Video>
)

data class Video(
        val key: String,
        val name: String
)