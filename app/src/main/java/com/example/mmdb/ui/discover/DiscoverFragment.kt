package com.example.mmdb.ui.discover

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mmdb.config.AppConfig
import com.example.mmdb.databinding.FragmentDiscoverBinding
import com.example.mmdb.extensions.requireAppConfig
import com.example.mmdb.extensions.requireNavController
import com.example.mmdb.navigation.*
import com.example.mmdb.navigation.actions.DiscoverFragmentAction
import com.example.mmdb.navigation.actions.MovieListType
import com.example.mmdb.navigation.actions.RemoteMovieGridFragmentAction
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.fragment_discover.*

object DiscoverFragmentArgs: ConfigFragmentArgs<DiscoverFragmentAction, DiscoverFragmentConfig>()

class DiscoverFragment : BaseNavigationFragment(), CategoryRecyclerView.AppBarTracking {

    private val action: DiscoverFragmentAction by action()
    private val config: DiscoverFragmentConfig by config()

    private lateinit var discoverViewModel: DiscoverViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentDiscoverBinding.inflate(inflater, container, false).apply {
            discoverViewModel = ViewModelProvider(
                this@DiscoverFragment,
                DiscoverViewModelFactory(appConfig, navController, action, config, toolbarViewModel)
            ).get(DiscoverViewModel::class.java)
            viewModel = discoverViewModel
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rangeSlider.addOnChangeListener { slider, value, _ ->
            discoverViewModel.onRangeSliderValueChanged(slider.activeThumbIndex == 0, value.toInt())
        }

        setupToolbar()
    }

    // --------- Toolbar functionality ------------//

    private var appBarOffset: Int = 0
    private var isAppBarIdle = false

    private var isExpanded: Boolean = false

    private fun setupToolbar() {
        app_bar.addOnOffsetChangedListener(
            AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
                appBarOffset = verticalOffset
                setToolbarArrowRotation(verticalOffset, appBarLayout)
                isExpanded = verticalOffset == 0
                // check beyond offset points to be safer
                isAppBarIdle = appBarOffset >= 0 || appBarOffset <= -app_bar.totalScrollRange
                if (isAppBarIdle)
                    setExpandAndCollapseEnabled(isExpanded)
            })

        expand_collapse_btn.setOnClickListener {
            isExpanded = !isExpanded
            app_bar.setExpanded(isExpanded, true)
        }
    }

    private fun setToolbarArrowRotation(verticalOffset: Int, appBarLayout: AppBarLayout) {
        // get percent of progress for scrolling done
        // current offset / positive max offset
        val progress = (-verticalOffset).toFloat() / appBarLayout.totalScrollRange
        arrow_image_view.rotation = 180 + progress * 180
    }

    private fun setExpandAndCollapseEnabled(enabled: Boolean) {
        categories_recycler_view.isNestedScrollingEnabled = enabled
    }

    override fun isAppBarExpanded(): Boolean = appBarOffset == 0
    override fun isAppBarIdle(): Boolean = isAppBarIdle
}