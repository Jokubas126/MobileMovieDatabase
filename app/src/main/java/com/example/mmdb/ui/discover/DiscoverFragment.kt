package com.example.mmdb.ui.discover

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mmdb.config.AppConfig
import com.example.mmdb.databinding.FragmentDiscoverBinding
import com.example.mmdb.extensions.requireAppConfig
import com.example.mmdb.extensions.requireNavController
import com.example.mmdb.navigation.ConfigFragmentArgs
import com.example.mmdb.navigation.NavigationController
import com.example.mmdb.navigation.action
import com.example.mmdb.navigation.actions.DiscoverFragmentAction
import com.example.mmdb.navigation.config
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.fragment_discover.*

object DiscoverFragmentArgs: ConfigFragmentArgs<DiscoverFragmentAction, DiscoverFragmentConfig>()

class DiscoverFragment : Fragment(), CategoryRecyclerView.AppBarTracking {

    private val appConfig: AppConfig by lazy {
        requireAppConfig()
    }

    private val navController: NavigationController by lazy {
        requireNavController()
    }

    private val action: DiscoverFragmentAction by action()
    private val config: DiscoverFragmentConfig by config()

    private lateinit var discoverViewModel: DiscoverViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentDiscoverBinding.inflate(inflater, container, false).apply {
            discoverViewModel = ViewModelProvider(
                this@DiscoverFragment,
                DiscoverViewModelFactory(appConfig, navController, action, config)
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

    /*override fun onMenuItemClick(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.action_confirm) {
            //discoverViewModel.onConfirmSelectionClicked(findNavController())
        }
        return true
    }*/

    // --------- Toolbar functionality ------------//

    private var appBarOffset: Int = 0
    private var isAppBarIdle = false

    private var isExpanded: Boolean = false

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        //menu.findItem(R.id.action_search).isVisible = false
        /*val confirmItem = menu.findItem(R.id.action_confirm)
        confirmItem.isVisible = true
        confirmItem.setOnMenuItemClickListener(this)*/
    }

    private fun setupToolbar() {
        //(activity as AppCompatActivity).supportActionBar!!.hide()
        //(activity as AppCompatActivity).setSupportActionBar(collapsing_toolbar)
        //activity!!.invalidateOptionsMenu()

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