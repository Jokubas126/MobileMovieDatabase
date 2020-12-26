package com.example.mmdb.ui.movielists.rest

import com.example.mmdb.managers.ProgressManager

class RemoteListProgressManager: ProgressManager() {

    var currentPage = 1
    var isListFull = false

    fun checkIfListFull(totalPages: Int) {
        if (currentPage == totalPages)
            isListFull = true
    }
}