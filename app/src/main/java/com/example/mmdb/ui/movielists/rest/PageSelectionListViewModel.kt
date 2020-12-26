package com.example.mmdb.ui.movielists.rest

import androidx.databinding.ObservableArrayList
import com.example.mmdb.BR
import com.example.mmdb.R
import me.tatarka.bindingcollectionadapter2.ItemBinding

class PageSelectionListViewModel(totalPages: Int, currentPage: Int = 1) {

    val itemsPage = ObservableArrayList<ItemPageViewModel>().apply {
        IntRange(1, totalPages).forEach {
            add(ItemPageViewModel(it, it == currentPage))
        }
    }
    val itemPageBinding: ItemBinding<ItemPageViewModel> =
        ItemBinding.of(BR.viewModel, R.layout.item_page)
}