package com.example.mmdb.ui.movielists.rest

data class ItemPageViewModel(
    val pageNumber: Int,
    val isCurrentPage: Boolean,
    val onSelected: (pageNumber: Int) -> Unit
)