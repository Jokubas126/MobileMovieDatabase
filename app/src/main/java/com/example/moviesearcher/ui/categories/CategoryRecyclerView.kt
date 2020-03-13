package com.example.moviesearcher.ui.categories

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**A RecyclerView that allows temporary pausing of causing its scroll to affect appBarLayout, based on https://stackoverflow.com/a/45338791/878126 */
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
        dx: Int, dy: Int, consumed: IntArray?, offsetInWindow: IntArray?,
        type: Int
    ): Boolean {
        if (type == ViewCompat.TYPE_NON_TOUCH && appBarTracking!!.isAppBarIdle()
            && isNestedScrollingEnabled
        ) {
            if (dy > 0) {
                if (appBarTracking!!.isAppBarExpanded()) {
                    consumed!![1] = dy
                    return true
                }
            } else {
                topPos = layoutManager!!.findFirstVisibleItemPosition()
                if (topPos == 0) {
                    view = layoutManager!!.findViewByPosition(topPos)
                    if (-view!!.top + dy <= 0) {
                        consumed!![1] = dy - view!!.top
                        return true
                    }
                }
            }
        }

        val returnValue = super.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow, type)
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