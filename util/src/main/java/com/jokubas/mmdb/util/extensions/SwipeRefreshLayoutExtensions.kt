package com.jokubas.mmdb.util.extensions

import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

@BindingAdapter("refreshListener")
fun SwipeRefreshLayout.setRefreshListener(listener: () -> Unit) {
    setOnRefreshListener {
        listener.invoke()
        isRefreshing = false
    }
}