package com.example.mmdb.ui.discover

import androidx.databinding.ObservableField
import androidx.lifecycle.*
import androidx.recyclerview.widget.ConcatAdapter
import com.jokubas.mmdb.ui_kit.ScrollingAppBarViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class DiscoverViewModel(
    private val discoverFragmentConfig: DiscoverFragmentConfig
) : ViewModel() {

    val scrollingAppBarViewModel = ScrollingAppBarViewModel(
        title = "Discover",
        onBackClicked = discoverFragmentConfig.onBackClicked,
        toolbarTools = discoverFragmentConfig.provideToolbarToolsView.invoke(),
        contentView = discoverFragmentConfig.provideToolbarContent.invoke(viewModelScope)
    )

    val categoriesAdapter = ObservableField<ConcatAdapter>()

    init {

        viewModelScope.launch(Dispatchers.IO) {
            discoverFragmentConfig.provideCategories.invoke().collect { response ->
                response.body()?.let { categoryList ->
                    categoriesAdapter.set(
                        ConcatAdapter(
                            categoryList.map { category ->
                                ItemsExpandableAdapter(category) { categoryType, subcategory ->
                                    discoverFragmentConfig.onSubcategoryClicked.invoke(
                                        categoryType,
                                        subcategory
                                    )
                                }
                            }
                        )
                    )
                } ?: run {
                    //TODO handle other DataResponse types
                }
            }
        }
    }
}