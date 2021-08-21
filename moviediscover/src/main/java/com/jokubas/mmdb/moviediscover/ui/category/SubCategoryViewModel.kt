package com.jokubas.mmdb.moviediscover.ui.category

import androidx.databinding.ObservableBoolean
import com.jokubas.mmdb.moviediscover.model.entities.CategoryType
import com.jokubas.mmdb.moviediscover.model.entities.Subcategory

data class SubCategoryViewModel(
    val subcategory: Subcategory,
    private val categoryType: CategoryType,
    private val onClick: ((categoryType: CategoryType, subcategory: Subcategory) -> Unit)?
) {

    val isChecked = ObservableBoolean(subcategory.isChecked)

    fun onItemClicked() {
        isChecked.set(!isChecked.get())
        subcategory.isChecked = isChecked.get()
        onClick?.invoke(categoryType, subcategory)
    }
}