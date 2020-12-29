package com.example.mmdb.ui.discover

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mmdb.databinding.ItemCategoryBinding
import com.example.mmdb.databinding.ItemSubcategoryBinding
import com.jokubas.mmdb.model.data.entities.Category
import com.jokubas.mmdb.model.data.entities.Subcategory

class ItemsExpandableAdapter(private val category: Category) :
    RecyclerView.Adapter<ItemsExpandableAdapter.ViewHolder>() {

    enum class ViewType {
        HEADER, ITEM
    }

    private var isExpanded = false

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) ViewType.HEADER.ordinal else ViewType.ITEM.ordinal
    }

    override fun getItemCount(): Int =
        if (isExpanded) category.subcategoryList.size + 1 else 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            ViewType.HEADER.ordinal -> ViewHolder.HeaderVH(
                ItemCategoryBinding.inflate(inflater, parent, false)
            )
            else -> ViewHolder.ItemVH(
                ItemSubcategoryBinding.inflate(inflater, parent, false)
            )

        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder.HeaderVH -> {
                holder.bind(category) {
                    isExpanded = !isExpanded
                    if (isExpanded) {
                        notifyItemRangeInserted(1, category.subcategoryList.size)
                    } else {
                        notifyItemRangeRemoved(1, category.subcategoryList.size)
                    }
                }
            }
            is ViewHolder.ItemVH -> holder.bind(category.subcategoryList[position - 1])
        }
    }

    sealed class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        class HeaderVH(private val binding: ItemCategoryBinding) : ViewHolder(binding.root) {

            fun bind(category: Category, onClick: () -> Unit) {
                binding.viewModel = CategoryViewModel(category.name, onClick)
            }
        }

        class ItemVH(private val binding: ItemSubcategoryBinding) : ViewHolder(binding.root) {

            fun bind(subcategory: Subcategory) {
                binding.viewModel = SubCategoryViewModel(subcategory.name)
            }
        }
    }
}