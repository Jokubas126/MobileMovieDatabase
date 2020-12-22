package com.example.mmdb.ui.discover

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.mmdb.databinding.ItemCategoryBinding
import com.example.mmdb.databinding.ItemSubcategoryBinding
import com.jokubas.mmdb.model.data.entities.Category
import com.jokubas.mmdb.model.data.entities.Subcategory
import com.thoughtbot.expandablecheckrecyclerview.CheckableChildRecyclerViewAdapter
import com.thoughtbot.expandablecheckrecyclerview.models.CheckedExpandableGroup
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup

class CategoryAdapter(groups: List<Category?>?) :
    CheckableChildRecyclerViewAdapter<CategoryViewHolder, SubcategoryViewHolder>(groups) {

    override fun onCreateGroupViewHolder(parent: ViewGroup?, viewType: Int): CategoryViewHolder {
        val binding = ItemCategoryBinding.inflate(
            LayoutInflater.from(parent!!.context),
            parent,
            false
        )
        return CategoryViewHolder(binding)
    }

    override fun onCreateCheckChildViewHolder(
        parent: ViewGroup?,
        viewType: Int
    ): SubcategoryViewHolder {
        val binding = ItemSubcategoryBinding.inflate(
            LayoutInflater.from(parent!!.context),
            parent,
            false)
        return SubcategoryViewHolder(binding)
    }

    override fun onBindGroupViewHolder(
        holder: CategoryViewHolder?,
        flatPosition: Int,
        group: ExpandableGroup<*>?
    ) {
        holder?.onBind(group as Category)
    }

    override fun onBindCheckChildViewHolder(
        holder: SubcategoryViewHolder?,
        flatPosition: Int,
        group: CheckedExpandableGroup?,
        childIndex: Int
    ) {
        group?.let{
            holder?.onBind(group.items[childIndex] as Subcategory)
        }
    }
}