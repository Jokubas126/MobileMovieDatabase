package com.example.mmdb.ui.details

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

sealed class IdWrapper: Parcelable {

    @Parcelize
    data class Remote(val id: Int): IdWrapper()

    @Parcelize
    data class Local(val id: Int): IdWrapper()
}