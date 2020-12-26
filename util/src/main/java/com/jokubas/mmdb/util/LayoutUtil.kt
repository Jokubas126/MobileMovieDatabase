package com.jokubas.mmdb.util

import android.content.res.Configuration
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

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

/*
@BindingAdapter(value = ["isDataLoading", "addData"], requireAll = true)
fun RecyclerView.loadDataOnScrolledDown(isDataLoading: Boolean, addData: () -> Unit) {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (!canScrollVertically(1) && !isDataLoading)
                addData.invoke()
        }
    })
}*/
