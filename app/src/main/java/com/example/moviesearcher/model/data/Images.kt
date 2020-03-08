package com.example.moviesearcher.model.data

import com.example.moviesearcher.util.KEY_BACKDROP_LIST
import com.example.moviesearcher.util.KEY_IMAGE_FILE_PATH
import com.example.moviesearcher.util.KEY_POSTER_LIST
import com.google.gson.annotations.SerializedName

class Images(
        @SerializedName(KEY_POSTER_LIST)
        val posterList: List<Image>,
        @SerializedName(KEY_BACKDROP_LIST)
        val backdropList: List<Image>
)

data class Image(
        @SerializedName(KEY_IMAGE_FILE_PATH)
        val filePath: String
)
