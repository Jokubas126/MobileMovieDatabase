package com.example.mmdb.ui.movielists.rest

import com.example.mmdb.managers.ProgressManager

class RemoteListProgressManager: ProgressManager() {

    var currentPage = 1
    var isListFull = false

    private var fetchedPage = 0

    fun addingData() {
        super.load()
        currentPage++
    }

    fun refresh() {
        super.load()
        currentPage = 1
        fetchedPage = 0
        isListFull = false
    }

    fun checkPages() {
        if (currentPage - fetchedPage > 1)
            currentPage = fetchedPage + 1
    }

    fun checkIfListFull(totalPages: Int) {
        if (currentPage == totalPages)
            isListFull = true
    }

    override fun success() {
        super.success()
        fetchedPage = currentPage
    }
}