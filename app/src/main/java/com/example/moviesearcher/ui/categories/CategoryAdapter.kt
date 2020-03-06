package com.example.moviesearcher.ui.categories

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.moviesearcher.R
import com.example.moviesearcher.model.data.Category
import com.example.moviesearcher.model.data.Subcategory
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter
import com.thoughtbot.expandablerecyclerview.listeners.OnGroupClickListener
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder
import kotlinx.android.synthetic.main.item_category.view.title_view

class CategoryAdapter(groups: List<Category>?, private val itemClickListener: OnSubcategoryClickedListener) :
        ExpandableRecyclerViewAdapter<CategoryAdapter.CategoryViewHolder, CategoryAdapter.SubcategoryViewHolder>(
                groups
        ) {

    interface OnSubcategoryClickedListener {
        fun onSubcategoryClicked(view: View?, subcategory: Subcategory?)
    }

    override fun onCreateGroupViewHolder(parent: ViewGroup?, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onCreateChildViewHolder(parent: ViewGroup?, viewType: Int): SubcategoryViewHolder {
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.item_subcategory, parent, false)
        return SubcategoryViewHolder(view)
    }

    override fun onBindGroupViewHolder(holder: CategoryViewHolder?, flatPosition: Int, group: ExpandableGroup<*>?) {
        holder!!.onBind(group as Category)
    }

    override fun onBindChildViewHolder(holder: SubcategoryViewHolder?, flatPosition: Int, group: ExpandableGroup<*>?, childIndex: Int) {
        holder!!.onBind(group!!.items[childIndex] as Subcategory)
    }

    inner class CategoryViewHolder(itemView: View) : GroupViewHolder(itemView) {
        fun onBind(category: Category){
            itemView.title_view.text = category.name
        }
    }

    inner class SubcategoryViewHolder(itemView: View) : ChildViewHolder(itemView) {
        private val view = itemView

        fun onBind(subcategory: Subcategory){
            view.title_view.text = subcategory.name
            view.setOnClickListener { itemClickListener.onSubcategoryClicked(it, subcategory) }
        }
    }
}