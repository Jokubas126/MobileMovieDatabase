package com.example.mmdb.ui.movielists.rest

import androidx.databinding.ObservableBoolean

data class ItemPageViewModel(
    val pageNumber: Int,
    val isCurrentPage: ObservableBoolean,
    val onSelected: (pageNumber: Int) -> Unit
)