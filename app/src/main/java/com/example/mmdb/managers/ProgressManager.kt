package com.example.mmdb.managers

import androidx.databinding.ObservableBoolean

open class ProgressManager {
    val loading = ObservableBoolean()
    val error = ObservableBoolean()

    fun loading() {
        loading.set(true)
        error.set(false)
    }

    fun loaded() {
        loading.set(false)
    }

    fun error() {
        loading.set(false)
        error.set(true)
    }

    fun success() {
        loading.set(false)
        error.set(false)
    }
}