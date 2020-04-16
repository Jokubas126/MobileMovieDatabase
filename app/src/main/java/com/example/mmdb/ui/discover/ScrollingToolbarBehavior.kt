package com.example.mmdb.ui.discover

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.appbar.AppBarLayout

class ScrollingToolbarBehavior(context: Context, attrs: AttributeSet) :
    AppBarLayout.Behavior(context, attrs) {
    override fun onInterceptTouchEvent(
        parent: CoordinatorLayout,
        child: AppBarLayout,
        ev: MotionEvent
    ): Boolean = false
}