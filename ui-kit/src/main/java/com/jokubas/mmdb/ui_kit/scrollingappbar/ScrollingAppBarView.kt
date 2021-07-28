package com.jokubas.mmdb.ui_kit.scrollingappbar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.appbar.AppBarLayout
import com.jokubas.mmdb.ui_kit.databinding.ScrollingToolbarViewBinding

class ScrollingAppBarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppBarLayout(context, attrs, defStyleAttr) {

    private var isBarExpanded: Boolean = true

    private var binding: ScrollingToolbarViewBinding? = null

    var scrollingAppBarViewModel: ScrollingAppBarViewModel? = null
        set(value) {
            field = value
            init()
        }

    fun init() {
        removeAllViews()
        binding = ScrollingToolbarViewBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        ).apply {
            viewModel = scrollingAppBarViewModel

            addOnOffsetChangedListener(
                OnOffsetChangedListener { appBarLayout, verticalOffset ->
                    arrowImageView.setToolbarArrowRotation(verticalOffset, appBarLayout)
                    isBarExpanded = verticalOffset == 0

                    //if app bar is idle invoke listener's event
                    if (verticalOffset >= 0 || verticalOffset <= -appBarLayout.totalScrollRange)
                        scrollingAppBarViewModel?.onAppBarIdle?.invoke(isBarExpanded)
                }
            )

            expandCollapseBtn.setOnClickListener {
                isBarExpanded = !isBarExpanded
                setExpanded(isBarExpanded, true)
            }
        }
    }

    private fun View.setToolbarArrowRotation(verticalOffset: Int, appBarLayout: AppBarLayout) {
        // get percent of progress for scrolling done
        // current offset / positive max offset
        val progress = (-verticalOffset).toFloat() / appBarLayout.totalScrollRange
        rotation = 180 + progress * 180
    }
}