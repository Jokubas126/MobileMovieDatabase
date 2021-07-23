package com.jokubas.mmdb.util.extensions

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.databinding.BindingAdapter

@BindingAdapter("childView")
fun FrameLayout.setChildView(view: View?) {
    view?.let {
        removeAllViews()

        // For reasons unknown, the view might have a parent.
        // We'll remove it before adding it to the FrameLayout.
        (view.parent as? ViewGroup)?.removeView(view)

        addView(view)
    }

}