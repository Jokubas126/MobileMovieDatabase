package com.example.mmdb.managers

import androidx.databinding.ObservableBoolean

open class ProgressManager {
    val loading = ObservableBoolean()
    val error = ObservableBoolean()

    open fun loading() {
        loading.set(true)
        error.set(false)
    }

    open fun load() {
        loading.set(true)
    }

    open fun loaded() {
        loading.set(false)
    }

    open fun error() {
        loading.set(false)
        error.set(true)
    }

    open fun success() {
        loading.set(false)
        error.set(false)
    }
}