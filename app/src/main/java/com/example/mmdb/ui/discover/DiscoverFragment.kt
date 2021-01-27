package com.example.mmdb.ui.discover

import android.os.Bundle
import android.view.*

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.mmdb.R
import com.example.mmdb.databinding.FragmentDiscoverBinding
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.fragment_discover.*

class DiscoverFragment : Fragment(), CategoryRecyclerView.AppBarTracking,
    MenuItem.OnMenuItemClickListener {

    private lateinit var viewModel: DiscoverViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true) // needed because toolbar is changed
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentDiscoverBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(DiscoverViewModel::class.java)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rangeSlider.addOnChangeListener { slider, value, _ ->
            viewModel.onRangeSliderValueChanged(slider.activeThumbIndex == 0, value.toInt())
        }

        setupToolbar()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.action_confirm) {
            viewModel.onConfirmSelectionClicked(findNavController())
        }
        return true
    }

    // --------- Toolbar functionality ------------//

    private var appBarOffset: Int = 0
    private var isAppBarIdle = false

    private var isExpanded: Boolean = false

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        //menu.findItem(R.id.action_search).isVisible = false
        val confirmItem = menu.findItem(R.id.action_confirm)
        confirmItem.isVisible = true
        confirmItem.setOnMenuItemClickListener(this)
    }

    private fun setupToolbar() {
        (activity as AppCompatActivity).supportActionBar!!.hide()
        (activity as AppCompatActivity).setSupportActionBar(collapsing_toolbar)
        activity!!.invalidateOptionsMenu()

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