package com.example.mmdb.ui.discover

import com.jokubas.mmdb.model.data.entities.Category
import com.jokubas.mmdb.model.data.entities.CategoryType

class CategoryViewModel(
    category: Category,
    val onClick: () -> Unit
) {
    val title = when (category.type) {
        CategoryType.GENRES -> "Genres"
        CategoryType.LANGUAGES -> "Languages"
        else -> ""
    }
}