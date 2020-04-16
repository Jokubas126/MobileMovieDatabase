package com.example.mmdb.ui.discover

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**A RecyclerView that allows temporary pausing of causing its scroll to affect appBarLayout, based on https://stackoverflow.com/a/45338791/8781 **/
class CategoryRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : RecyclerView(context, attrs, defStyle) {

    private var appBarTracking: AppBarTracking? = null
    private var view: View? = null
    private var topPos: Int = 0
    private var layoutManager: LinearLayoutManager? = null

    interface AppBarTracking {
        fun isAppBarIdle(): Boolean
        fun isAppBarExpanded(): Boolean
    }

    override fun dispatchNestedPreScroll(
        distanceX: Int, distanceY: Int, consumed: IntArray?, offsetInWindow: IntArray?,
        type: Int
    ): Boolean {

        // User is not touching a screen, appbar is idle (fully collapsed or fully expanded)
        // and nested scrolling is enabled
        if (type == ViewCompat.TYPE_NON_TOUCH && appBarTracking!!.isAppBarIdle()
            && isNestedScrollingEnabled
        ) {
            // SCROLLING DOWN
            if (distanceY > 0) {
                // if appbar expanded then the consumed vertical distance should be
                // the same as moved distance and movement should be consumed (return true)
                if (appBarTracking!!.isAppBarExpanded()) {
                    consumed!![1] = distanceY
                    return true
                }
            } else {
                // SCROLLING UP
                // first visible view position from adapter
                topPos = layoutManager!!.findFirstVisibleItemPosition()
                if (topPos == 0) {
                    // if it's a very first item, take its' view
                    view = layoutManager!!.findViewByPosition(topPos)
                    // check if scrolled distance does not go beyond the view's top
                    // (in case view's top is at negative position)
                    if (distanceY <= view!!.top) {
                        // consume the distance of scroll + as much as needed to the view's top
                        // to prevent lag at the top
                        consumed!![1] = distanceY - view!!.top
                        return true
                    }
                }
            }
        }

        val returnValue =
            super.dispatchNestedPreScroll(distanceX, distanceY, consumed, offsetInWindow, type)
        // to keep view coordinate offset at 0 when nested scrolling disabled
        // (preventing unwanted scrolling behavior)
        if (offsetInWindow != null && !isNestedScrollingEnabled && offsetInWindow[1] != 0)
            offsetInWindow[1] = 0
        return returnValue
    }

    override fun setLayoutManager(layout: LayoutManager?) {
        super.setLayoutManager(layout)
        layoutManager = layout as LinearLayoutManager
    }

    fun setAppBarTracking(appBarTracking: AppBarTracking) {
        this.appBarTracking = appBarTracking
    }

}