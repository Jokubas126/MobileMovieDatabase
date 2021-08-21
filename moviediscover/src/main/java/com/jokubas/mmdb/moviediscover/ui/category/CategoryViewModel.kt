package com.jokubas.mmdb.moviediscover.ui.category

import com.jokubas.mmdb.moviediscover.model.entities.Category
import com.jokubas.mmdb.moviediscover.model.entities.CategoryType

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