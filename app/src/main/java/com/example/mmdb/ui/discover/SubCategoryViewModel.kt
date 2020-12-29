package com.example.mmdb.ui.discover

import androidx.databinding.ObservableBoolean
import com.jokubas.mmdb.model.data.entities.CategoryType
import com.jokubas.mmdb.model.data.entities.Subcategory

data class SubCategoryViewModel(
    val subcategory: Subcategory,
    private val categoryType: CategoryType,
    private val onClick: ((categoryType: CategoryType, subcategory: Subcategory, isChecked: Boolean) -> Unit)?
) {

    val isChecked = ObservableBoolean(false)

    fun onItemClicked() {
        isChecked.set(!isChecked.get())
        onClick?.invoke(categoryType, subcategory, isChecked.get())
    }
}