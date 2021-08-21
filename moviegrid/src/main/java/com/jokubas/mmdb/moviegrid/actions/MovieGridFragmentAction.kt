package com.jokubas.mmdb.moviegrid.actions

import com.jokubas.mmdb.util.navigationtools.WrappedFragmentAction
import kotlinx.parcelize.Parcelize

@Parcelize
class MovieGridFragmentAction(
    val movieListType: MovieListType = MovieListType.Remote.Popular
) : WrappedFragmentAction