package com.example.mmdb.util

import android.content.Context
import android.content.res.Configuration
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

fun getMovieGridLayoutManager(context: Context?): RecyclerView.LayoutManager {
    return context?.resources?.configuration?.orientation?.let {
        if (it == Configuration.ORIENTATION_LANDSCAPE)
            StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL)
        else
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
    } ?: run { StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL) }
}