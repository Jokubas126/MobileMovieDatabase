package com.example.mmdb.ui.discover

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Checkable
import android.widget.CheckedTextView
import com.example.mmdb.R
import com.example.mmdb.model.data.Category
import com.example.mmdb.model.data.Subcategory
import com.thoughtbot.expandablecheckrecyclerview.CheckableChildRecyclerViewAdapter
import com.thoughtbot.expandablecheckrecyclerview.models.CheckedExpandableGroup
import com.thoughtbot.expandablecheckrecyclerview.viewholders.CheckableChildViewHolder
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder
import kotlinx.android.synthetic.main.item_category.view.*
import kotlinx.android.synthetic.main.item_subcategory.view.*

class CategoryAdapter(groups: List<Category?>?) :
    CheckableChildRecyclerViewAdapter<CategoryAdapter.CategoryViewHolder, CategoryAdapter.SubcategoryViewHolder>(
        groups
    ) {

    override fun onCreateGroupViewHolder(parent: ViewGroup?, viewType: Int): CategoryViewHolder {
        val view =
            LayoutInflater.from(parent!!.context).inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onCreateCheckChildViewHolder(
        parent: ViewGroup?,
        viewType: Int
    ): SubcategoryViewHolder {
        val view =
            LayoutInflater.from(parent!!.context).inflate(R.layout.item_subcategory, parent, false)
        return SubcategoryViewHolder(view)
    }

    override fun onBindGroupViewHolder(
        holder: CategoryViewHolder?,
        flatPosition: Int,
        group: ExpandableGroup<*>?
    ) {
        holder!!.onBind(group as Category)
    }

    override fun onBindCheckChildViewHolder(
        holder: SubcategoryViewHolder?,
        flatPosition: Int,
        group: CheckedExpandableGroup?,
        childIndex: Int
    ) {
        holder!!.onBind(group!!.items[childIndex] as Subcategory)
    }

    inner class CategoryViewHolder(itemView: View) : GroupViewHolder(itemView) {
        fun onBind(category: Category) {
            itemView.category_title_view.text = category.name
        }
    }

    inner class SubcategoryViewHolder(itemView: View) : CheckableChildViewHolder(itemView) {
        private val view = itemView
        private val subcategoryTextView: CheckedTextView = view.subcategory_title_view

        fun onBind(subcategory: Subcategory) {
            subcategoryTextView.text = subcategory.name
        }

        override fun getCheckable(): Checkable {
            return subcategoryTextView
        }
    }
}