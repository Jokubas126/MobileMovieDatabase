package com.example.mmdb.ui.discover

import androidx.databinding.ObservableField
import androidx.lifecycle.*
import androidx.recyclerview.widget.ConcatAdapter
import com.example.mmdb.ui.ToolbarViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.DecimalFormatSymbols

class DiscoverViewModel(
    private val discoverFragmentConfig: DiscoverFragmentConfig,
    val toolbarViewModel: ToolbarViewModel
) : ViewModel() {

    val onRangeSliderValueChangedListener = discoverFragmentConfig.onRangeSliderValueChangedListener

    val valueFrom: Float = discoverFragmentConfig.startYearFlow.value.toFloat()
    val valueTo: Float = discoverFragmentConfig.endYearFlow.value.toFloat()

    val startYear: ObservableField<String> = ObservableField(valueFrom.toInt().toString())
    val endYear: ObservableField<String> = ObservableField(valueTo.toInt().toString())

    val categoriesAdapter = ObservableField<ConcatAdapter>()

    init {
        toolbarViewModel.setClickListener(discoverFragmentConfig.toolbarClickListener)
        viewModelScope.launch(Dispatchers.IO) {
            combine(
                discoverFragmentConfig.startYearFlow,
                discoverFragmentConfig.endYearFlow
            ) { start, end -> Pair(start, end) }.collect { (start, end) ->
                startYear.set(
                    start.takeUnless {
                        start == valueFrom.toInt()
                    }?.toString() ?: DecimalFormatSymbols.getInstance().infinity
                )
                endYear.set(end.toString())
            }
        }
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