package com.example.mmdb.ui.drawer

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
enum class DrawerAction : Parcelable {
    Open,
    Close
}