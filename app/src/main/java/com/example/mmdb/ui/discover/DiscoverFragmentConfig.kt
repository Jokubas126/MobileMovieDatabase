package com.example.mmdb.ui.discover

import android.view.View
import com.example.mmdb.ui.ToolbarViewModel
import com.google.android.material.slider.RangeSlider
import com.jokubas.mmdb.model.data.entities.Category
import com.jokubas.mmdb.model.data.entities.CategoryType
import com.jokubas.mmdb.model.data.entities.Subcategory
import com.jokubas.mmdb.util.DataResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

class DiscoverFragmentConfig(
    val provideCategories: () -> Flow<DataResponse<List<Category>>>,
    val onSubcategoryClicked: (categoryType: CategoryType, subcategory: Subcategory) -> Unit,
    val onBackClicked: () -> Unit,
    val provideToolbarToolsView: () -> View?,
    val provideToolbarContent: (coroutineScope: CoroutineScope) -> View?
)