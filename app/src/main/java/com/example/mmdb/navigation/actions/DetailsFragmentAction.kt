package com.example.mmdb.navigation.actions

import com.example.mmdb.navigation.Action
import kotlinx.android.parcel.Parcelize

@Parcelize
class DetailsFragmentAction(
    val movieId: Int,
    val isRemote: Boolean
): Action