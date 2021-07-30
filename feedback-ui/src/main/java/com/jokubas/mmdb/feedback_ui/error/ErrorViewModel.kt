package com.jokubas.mmdb.feedback_ui.error

import android.view.View

open class ErrorViewModel(
    val title: String,
    val description: String,
    val buttonConfig: ErrorButtonConfig? = null
) {

    val buttonVisibility: Int = buttonConfig?.let { View.VISIBLE } ?: View.GONE
}