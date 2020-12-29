package com.jokubas.mmdb.util.extensions

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView

@BindingAdapter("adapter")
fun RecyclerView.setConcatAdapter(adapter: ConcatAdapter?) {
    adapter?.let { setAdapter(adapter) }
}