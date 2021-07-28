package com.jokubas.mmdb.ui_kit.scrollingappbar

import android.view.View
import androidx.databinding.ObservableBoolean

class ScrollingAppBarViewModel(
    val title: String,
    val onBackClicked: () -> Unit,
    val toolbarTools: View? = null,
    val contentView: View? = null
) {

    val isCategoryListNesterScrollingEnabled = ObservableBoolean(true)

    val onAppBarIdle = { isExpanded: Boolean ->
        isCategoryListNesterScrollingEnabled.set(isExpanded)
    }
}