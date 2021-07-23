package com.jokubas.mmdb.ui_kit

import android.view.View

class ScrollingAppBarViewModel(
    val title: String,
    val onBackClicked: () -> Unit,
    val toolbarTools: View? = null,
    val contentView: View? = null
)