package com.jokubas.mmdb.util.extensions

import androidx.databinding.BindingAdapter
import com.google.android.material.appbar.AppBarLayout

@BindingAdapter("onOffsetChangedListener")
fun AppBarLayout.setOnOffsetChangedListener(listener: AppBarLayout.OnOffsetChangedListener) {
    addOnOffsetChangedListener(listener)
}