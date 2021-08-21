package com.jokubas.mmdb.moviedetails.actions

import com.jokubas.mmdb.util.navigationtools.Action
import kotlinx.parcelize.Parcelize

@Parcelize
class DetailsFragmentAction(
    val movieId: Int,
    val isRemote: Boolean
): Action