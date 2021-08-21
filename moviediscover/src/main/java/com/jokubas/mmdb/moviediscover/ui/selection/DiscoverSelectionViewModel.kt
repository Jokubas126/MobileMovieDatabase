package com.jokubas.mmdb.moviediscover.ui.selection

import androidx.databinding.ObservableArrayList
import com.jokubas.mmdb.moviediscover.BR
import com.jokubas.mmdb.moviediscover.R
import me.tatarka.bindingcollectionadapter2.ItemBinding

class DiscoverSelectionViewModel(
    private val discoverSelections: List<String?> = emptyList()
) {

    val items = ObservableArrayList<ItemDiscoverSelectionViewModel>().apply {
        addAll(
            discoverSelections.mapNotNull {
                it?.let { ItemDiscoverSelectionViewModel(it) }
            }
        )
    }
    val itemBinding: ItemBinding<ItemDiscoverSelectionViewModel> =
        ItemBinding.of(BR.viewModel, R.layout.item_discover_selection)
}