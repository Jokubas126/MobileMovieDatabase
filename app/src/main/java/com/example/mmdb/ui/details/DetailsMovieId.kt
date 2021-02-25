package com.example.mmdb.ui.details

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

sealed class DetailsMovieId: Parcelable {

    @Parcelize
    data class Remote(val id: Int): DetailsMovieId()

    @Parcelize
    data class Local(val id: Int): DetailsMovieId()
}