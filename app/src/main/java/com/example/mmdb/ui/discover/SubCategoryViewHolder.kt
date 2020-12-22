package com.example.mmdb.ui.discover

import android.widget.Checkable
import com.example.mmdb.databinding.ItemSubcategoryBinding
import com.jokubas.mmdb.model.data.entities.Subcategory
import com.thoughtbot.expandablecheckrecyclerview.viewholders.CheckableChildViewHolder
import kotlinx.android.synthetic.main.item_subcategory.view.*

class SubcategoryViewHolder(private val binding: ItemSubcategoryBinding) :
    CheckableChildViewHolder(binding.root) {

    fun onBind(subcategory: Subcategory) {
        binding.viewModel = SubCategoryViewModel(subcategory.name)
    }

    override fun getCheckable(): Checkable {
        return binding.root.subcategory_title_view
    }
}