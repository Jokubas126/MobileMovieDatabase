package com.jokubas.mmdb.ui_kit

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**A RecyclerView that allows temporary pausing of causing its scroll to affect appBarLayout, based on https://stackoverflow.com/a/45338791/8781 **/
class CategoryRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : RecyclerView(context, attrs, defStyle) {

    //private var appBarTracking: ScrollingToolbarView.AppBarTracking? = null
    private var layoutManager: LinearLayoutManager? = null

    override fun dispatchNestedPreScroll(
        distanceX: Int, distanceY: Int, consumed: IntArray?, offsetInWindow: IntArray?, type: Int
    ): Boolean {
        // to keep view coordinate offset at 0 when nested scrolling disabled (toolbar collapsed and idle)
        // (preventing unwanted scrolling behavior)
        if (offsetInWindow != null && !isNestedScrollingEnabled && offsetInWindow[1] != 0)
            offsetInWindow[1] = 0
        return super.dispatchNestedPreScroll(distanceX, distanceY, consumed, offsetInWindow, type)
    }

    override fun setLayoutManager(layout: LayoutManager?) {
        super.setLayoutManager(layout)
        layoutManager = layout as LinearLayoutManager
    }

    /*fun setAppBarTracking(appBarTracking: ScrollingToolbarView.AppBarTracking) {
        this.appBarTracking = appBarTracking
    }*/
}