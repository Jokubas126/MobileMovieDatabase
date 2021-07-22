package com.example.mmdb.ui.discover

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**A RecyclerView that allows temporary pausing of causing its scroll to affect appBarLayout, based on https://stackoverflow.com/a/45338791/8781 **/
class CategoryRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : RecyclerView(context, attrs, defStyle) {

    private var appBarTracking: AppBarTracking? = null
    private var layoutManager: LinearLayoutManager? = null

    interface AppBarTracking {
        fun isAppBarIdle(): Boolean
        fun isAppBarExpanded(): Boolean
    }

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

    fun setAppBarTracking(appBarTracking: AppBarTracking) {
        this.appBarTracking = appBarTracking
    }

}