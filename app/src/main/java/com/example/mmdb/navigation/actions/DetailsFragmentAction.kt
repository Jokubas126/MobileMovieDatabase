package com.example.mmdb.navigation.actions

import com.example.mmdb.navigation.Action
import com.example.mmdb.ui.details.DetailsMovieId
import kotlinx.android.parcel.Parcelize

@Parcelize
class DetailsFragmentAction(
    val movieId: DetailsMovieId
): Action