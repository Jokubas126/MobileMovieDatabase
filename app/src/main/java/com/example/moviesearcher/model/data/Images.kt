package com.example.moviesearcher.model.data

import com.google.gson.annotations.SerializedName

class Images(
        @SerializedName("posters")
        val posterList: List<Image>,
        @SerializedName("backdrops")
        val backdropList: List<Image>
)

data class Image(
        @SerializedName("file_path")
        val filePath: String
)
