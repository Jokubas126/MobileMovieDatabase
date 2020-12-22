package com.example.mmdb.ui.discover

import com.example.mmdb.databinding.ItemCategoryBinding
import com.jokubas.mmdb.model.data.entities.Category
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder

class CategoryViewHolder(private val binding: ItemCategoryBinding) : GroupViewHolder(binding.root) {
    fun onBind(category: Category) {
        binding.viewModel = CategoryViewModel(category.name)
    }
}