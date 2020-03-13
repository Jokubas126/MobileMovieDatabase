package com.example.moviesearcher.ui.categories

import android.os.Bundle
import android.view.*

import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviesearcher.R
import com.example.moviesearcher.model.data.Category
import com.example.moviesearcher.model.data.Subcategory
import com.example.moviesearcher.ui.categories.CategoryAdapter.OnSubcategoryClickedListener
import com.google.android.material.appbar.AppBarLayout
import io.apptik.widget.MultiSlider
import io.apptik.widget.MultiSlider.OnThumbValueChangeListener
import io.apptik.widget.MultiSlider.Thumb
import kotlinx.android.synthetic.main.fragment_categories.*

class CategoriesFragment : Fragment(), OnThumbValueChangeListener, OnSubcategoryClickedListener, CategoryRecyclerView.AppBarTracking {

    private lateinit var recyclerView: CategoryRecyclerView

    private lateinit var viewModel: CategoriesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_categories, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(CategoriesViewModel::class.java)
        viewModel.fetch()

        release_year_slider.setOnThumbValueChangeListener(this)

        recyclerView = categories_recycler_view
        recyclerView.layoutManager = LinearLayoutManager(context)
        setupToolbar()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.categories.observe(
            viewLifecycleOwner,
            Observer<List<Category>> { categories: List<Category>? ->
                if (categories != null)
                    recyclerView.adapter = CategoryAdapter(categories, this)
                ViewCompat.setNestedScrollingEnabled(recyclerView, false)
            })
    }

    override fun onValueChanged(
        multiSlider: MultiSlider,
        thumb: Thumb?,
        thumbIndex: Int,
        value: Int
    ) {
        //thumbIndexes are slider sides (0 is left and 1 is right side)
        if (thumbIndex == 0)
            if (value != multiSlider.min)
                release_year_slider_min_value.text = value.toString()
            else release_year_slider_min_value.text = "∞"
        if (thumbIndex == 1) {
            release_year_slider_max_value.text = value.toString()
        }
    }



    override fun onSubcategoryClicked(view: View?, subcategory: Subcategory, categoryName: String) {
        viewModel.onSubcategoryClicked(
            view,
            subcategory,
            categoryName,
            release_year_slider_min_value.text.toString(),
            release_year_slider_max_value.text.toString()
        )
    }

    private var mAppBarOffset: Int = 0
    private var mAppBarIdle = false
    private var mAppBarMaxOffset: Int = 0

    private var isExpanded: Boolean = false

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.action_confirm).isVisible = true
        menu.findItem(R.id.action_search).isVisible = false
    }

    private fun setupToolbar(){
        (activity as AppCompatActivity).supportActionBar!!.hide()
        (activity as AppCompatActivity).setSupportActionBar(collapsing_toolbar)
        activity!!.invalidateOptionsMenu()
        app_bar.addOnOffsetChangedListener(
            AppBarLayout.OnOffsetChangedListener {
                    appBarLayout, verticalOffset ->
            mAppBarOffset = verticalOffset
            val totalScrollRange = appBarLayout.totalScrollRange
            val progress = (-verticalOffset).toFloat() / totalScrollRange
            arrowImageView.rotation = 180 + progress * 180
            isExpanded = verticalOffset == 0;
            mAppBarIdle = mAppBarOffset >= 0 || mAppBarOffset <= mAppBarMaxOffset
            if (mAppBarIdle)
                setExpandAndCollapseEnabled(isExpanded)
        })

        app_bar.post(Runnable { mAppBarMaxOffset = -app_bar.totalScrollRange })
        recyclerView.setAppBarTracking(this)

        expandCollapseButton.setOnClickListener {
            isExpanded = !isExpanded
            app_bar.setExpanded(isExpanded, true)
        }
    }

    private fun setExpandAndCollapseEnabled(enabled: Boolean) {
        recyclerView.isNestedScrollingEnabled = enabled
    }

    override fun isAppBarExpanded(): Boolean = mAppBarOffset == 0
    override fun isAppBarIdle(): Boolean = mAppBarIdle
}