package com.jokubas.mmdb.ui_kit

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import com.google.android.material.appbar.AppBarLayout
import com.jokubas.mmdb.ui_kit.databinding.ScrollingToolbarViewBinding

class ScrollingToolbarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppBarLayout(context, attrs, defStyleAttr) {

    private var appBarOffset: Int = 0
    private var isAppBarIdle = false

    private var isBarExpanded: Boolean = false

    private var binding: ScrollingToolbarViewBinding? = null

    var scrollingAppBarViewModel: ScrollingAppBarViewModel? = null
        set(value) {
            field = value
            init()
        }

    fun init() {
        binding = ScrollingToolbarViewBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        ).apply {
            viewModel = scrollingAppBarViewModel

            addOnOffsetChangedListener(
                OnOffsetChangedListener { appBarLayout, verticalOffset ->
                    appBarOffset = verticalOffset
                    arrowImageView.setToolbarArrowRotation(verticalOffset, appBarLayout)
                    isBarExpanded = verticalOffset == 0
                    // check beyond offset points to be safer
                    isAppBarIdle =
                        appBarOffset >= 0 || appBarOffset <= -appBarLayout.totalScrollRange
                    if (isAppBarIdle)
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