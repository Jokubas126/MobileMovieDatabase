package com.example.mmdb.ui.movielists.discover

import androidx.databinding.ObservableArrayList
import com.example.mmdb.BR
import com.example.mmdb.R
import me.tatarka.bindingcollectionadapter2.ItemBinding

class DiscoverSelectionViewModel(
    private val discoverSelections: List<String?>
) {

    val items = ObservableArrayList<ItemDiscoverSelectionViewModel>().apply {
        addAll(
            discoverSelections.map {
                it?.let { ItemDiscoverSelectionViewModel(it) }
            }
        )
    }
    val itemBinding: ItemBinding<ItemDiscoverSelectionViewModel> =
        ItemBinding.of(BR.viewModel, R.layout.item_discover_selection)
}