package com.example.mmdb.ui.discover

import android.view.View
import androidx.databinding.ObservableBoolean
import com.jokubas.mmdb.ui_kit.ScrollingAppBarViewModel

class DiscoverScrollingAppBarViewModel(
    title: String,
    onBackClicked: () -> Unit,
    toolbarTools: View?,
    contentView: View?
) {

    val scrollingAppBarViewModel = ScrollingAppBarViewModel(
        title = title,
        onBackClicked = onBackClicked,
        toolbarTools = toolbarTools,
        contentView = contentView
    )

    val isCategoryListNesterScrollingEnabled = ObservableBoolean(false)

    val isBarIdleListener: ((Boolean) -> Unit) = { isExpanded ->
        isCategoryListNesterScrollingEnabled.set(isExpanded)
    }
}