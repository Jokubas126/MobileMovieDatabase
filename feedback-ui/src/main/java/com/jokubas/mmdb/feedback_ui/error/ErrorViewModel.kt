package com.jokubas.mmdb.feedback_ui.error

import android.view.View
import com.jokubas.mmdb.ui_kit.bindingclasses.ImageBind

open class ErrorViewModel(
    val icon: ImageBind,
    val title: String,
    val description: String,
    val buttonConfig: ErrorButtonConfig? = null
) {

    val buttonVisibility: Int = buttonConfig?.let { View.VISIBLE } ?: View.GONE
}