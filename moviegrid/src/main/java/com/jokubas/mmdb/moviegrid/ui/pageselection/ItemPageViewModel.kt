package com.jokubas.mmdb.moviegrid.ui.pageselection

import androidx.databinding.ObservableBoolean

data class ItemPageViewModel(
    val pageNumber: Int,
    val isCurrentPage: ObservableBoolean,
    val onSelected: () -> Unit
)