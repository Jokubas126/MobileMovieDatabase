package com.jokubas.mmdb.moviediscover.ui.discover

import android.view.View
import com.jokubas.mmdb.model.data.entities.Category
import com.jokubas.mmdb.model.data.entities.CategoryType
import com.jokubas.mmdb.model.data.entities.Subcategory
import com.jokubas.mmdb.util.DataResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

class DiscoverFragmentConfig(
    val provideCategories: () -> Flow<DataResponse<List<Category>>>,
    val onSubcategoryClicked: (categoryType: CategoryType, subcategory: Subcategory) -> Unit,
    val onBackClicked: () -> Unit,
    val provideToolbarToolsView: () -> View?,
    val provideToolbarContent: (coroutineScope: CoroutineScope) -> View?
)