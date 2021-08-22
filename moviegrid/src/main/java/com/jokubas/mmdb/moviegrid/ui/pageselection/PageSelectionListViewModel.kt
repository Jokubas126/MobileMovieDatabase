package com.jokubas.mmdb.moviegrid.ui.pageselection

import android.view.View
import androidx.databinding.Observable
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableInt
import com.jokubas.mmdb.moviegrid.BR
import com.jokubas.mmdb.moviegrid.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import me.tatarka.bindingcollectionadapter2.ItemBinding
import kotlin.math.max

class PageSelectionListViewModel(collectCoroutineScope: CoroutineScope) {

    val pageSelectionListVisibility: ObservableInt = ObservableInt(View.GONE)

    private val _currentPage: MutableStateFlow<Int> = MutableStateFlow(1)
    val currentPage: StateFlow<Int> = _currentPage.asStateFlow()

    private val totalPages: ObservableInt = ObservableInt(1)

    val itemsPage = ObservableArrayList<ItemPageViewModel>()

    val itemPageBinding: ItemBinding<ItemPageViewModel> =
        ItemBinding.of(BR.viewModel, R.layout.item_page)

    init {
        collectCoroutineScope.launch {
            currentPage.collect { page ->
                itemsPage.find { it.isCurrentPage.get() }?.isCurrentPage?.set(false)
                itemsPage.find { it.pageNumber == page }?.isCurrentPage?.set(true)
            }
        }
        totalPages.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                itemsPage.removeAll {
                    it.pageNumber > totalPages.get()
                }
                populatePageSelectionList()
            }
        })
    }

    private fun populatePageSelectionList() {
        if(itemsPage.size < totalPages.get())
            IntRange(max(1, itemsPage.size), totalPages.get()).forEach {
                itemsPage.add(
                    ItemPageViewModel(
                        pageNumber = it,
                        isCurrentPage = ObservableBoolean(it == currentPage.value),
                        onSelected = {
                            _currentPage.value = it
                        }
                    )
                )
            }
    }

    fun update(currentPage: Int, totalPages: Int) {
        if (this.currentPage.value != currentPage)
            this._currentPage.value = currentPage

        if (this.totalPages.get() != totalPages)
            this.totalPages.set(totalPages)

        pageSelectionListVisibility.set(if (totalPages > 1) View.VISIBLE else View.GONE)
    }
}
