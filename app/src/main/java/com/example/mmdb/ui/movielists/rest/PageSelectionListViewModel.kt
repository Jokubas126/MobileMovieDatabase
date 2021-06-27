package com.example.mmdb.ui.movielists.rest

import androidx.databinding.Observable
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableInt
import com.example.mmdb.BR
import com.example.mmdb.R
import me.tatarka.bindingcollectionadapter2.ItemBinding

class PageSelectionListViewModel(private val onSelected: (pageNumber: Int) -> Unit) {

    private val currentPage: ObservableInt = ObservableInt(1)

    private val totalPages: ObservableInt = ObservableInt(0)

    val itemsPage = ObservableArrayList<ItemPageViewModel>()

    val itemPageBinding: ItemBinding<ItemPageViewModel> =
        ItemBinding.of(BR.viewModel, R.layout.item_page)

    init {
        currentPage.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                itemsPage.find { it.isCurrentPage.get() }?.isCurrentPage?.set(false)
                itemsPage.find { it.pageNumber == currentPage.get() }?.isCurrentPage?.set(true)
            }
        })
        totalPages.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                itemsPage.removeAll { true }
                populatePageSelectionList()
            }
        })
    }

    private fun populatePageSelectionList() {
        IntRange(1, totalPages.get()).forEach {
            itemsPage.add(
                ItemPageViewModel(
                    pageNumber = it,
                    isCurrentPage = ObservableBoolean(it == currentPage.get()),
                    onSelected = onSelected
                )
            )
        }
    }

    fun update(currentPage: Int, totalPages: Int) {
        if (this.currentPage.get() != currentPage)
            this.currentPage.set(currentPage)

        if (this.totalPages.get() != totalPages)
            this.totalPages.set(totalPages)
    }
}
