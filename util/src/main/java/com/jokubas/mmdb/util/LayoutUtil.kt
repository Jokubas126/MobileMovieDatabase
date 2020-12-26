package com.jokubas.mmdb.util

import android.content.res.Configuration
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

//TODO set something up for restoring the state
@BindingAdapter("movieGridLayoutManager")
fun RecyclerView.setMovieGridLayoutManager(landscapeSpanCount: Int = 4) {
    layoutManager = when (context.resources.configuration.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> StaggeredGridLayoutManager(
            landscapeSpanCount,
            StaggeredGridLayoutManager.VERTICAL
        )
        else -> StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
    }
}