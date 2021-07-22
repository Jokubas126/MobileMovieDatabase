package com.example.mmdb.ui.discover

import com.example.mmdb.ui.ToolbarViewModel
import com.google.android.material.slider.RangeSlider
import com.jokubas.mmdb.model.data.entities.Category
import com.jokubas.mmdb.model.data.entities.CategoryType
import com.jokubas.mmdb.model.data.entities.Subcategory
import com.jokubas.mmdb.util.DataResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

class DiscoverFragmentConfig(
    val provideCategories: () -> Flow<DataResponse<List<Category>>>,
    val startYearFlow: StateFlow<Int>,
    val endYearFlow: StateFlow<Int>,
    val onRangeSliderValueChangedListener: RangeSlider.OnChangeListener,
    val onSubcategoryClicked: (categoryType: CategoryType, subcategory: Subcategory) -> Unit,
    val toolbarClickListener: ToolbarViewModel.ClickListener
)