package com.example.mmdb.navigation.actions

import com.example.mmdb.navigation.Action
import com.example.mmdb.ui.details.IdWrapper
import kotlinx.android.parcel.Parcelize

@Parcelize
class DetailsFragmentAction(
    val idWrapper: IdWrapper
): Action