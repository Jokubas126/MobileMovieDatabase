package com.example.moviesearcher.ui.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesearcher.R
import com.example.moviesearcher.model.data.Category
import com.example.moviesearcher.model.data.Subcategory
import com.example.moviesearcher.ui.categories.CategoryAdapter.OnSubcategoryClickedListener
import io.apptik.widget.MultiSlider
import io.apptik.widget.MultiSlider.OnThumbValueChangeListener
import io.apptik.widget.MultiSlider.Thumb
import kotlinx.android.synthetic.main.fragment_categories.*

class CategoriesFragment : Fragment(), OnThumbValueChangeListener, OnSubcategoryClickedListener {

    private lateinit var recyclerView: RecyclerView

    private lateinit var viewModel: CategoriesViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_categories, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(CategoriesViewModel::class.java)
        viewModel.fetch()

        release_year_slider.setOnThumbValueChangeListener(this)

        recyclerView = categories_recycler_view
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.categories.observe(viewLifecycleOwner, Observer<List<Category>> { categories: List<Category>? ->
            if (categories != null)
                recyclerView.adapter = CategoryAdapter(categories, this)

        })
        viewModel.loading.observe(viewLifecycleOwner, Observer { isLoading: Boolean? ->
            if (isLoading != null) {
                progress_bar.visibility = if (isLoading) View.VISIBLE else View.GONE
                recyclerView.visibility = if (isLoading) View.GONE else View.VISIBLE
            }
        })
    }

    override fun onValueChanged(multiSlider: MultiSlider, thumb: Thumb?, thumbIndex: Int, value: Int) {
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
        viewModel.onSubcategoryClicked(view, subcategory, categoryName, release_year_slider_min_value.text.toString(), release_year_slider_max_value.text.toString())
    }
}