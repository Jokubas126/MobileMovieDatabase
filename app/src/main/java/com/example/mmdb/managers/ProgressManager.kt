package com.example.mmdb.managers

import androidx.lifecycle.MutableLiveData

open class ProgressManager {
    val loading = MutableLiveData<Boolean>()
    val error = MutableLiveData<Boolean>()

    open fun loading() {
        loading.postValue(true)
        error.postValue(false)
    }

    open fun load() {
        loading.postValue(true)
    }

    open fun loaded() {
        loading.postValue(false)
    }

    open fun error() {
        loading.postValue(false)
        error.postValue(true)
    }

    open fun success() {
        loading.postValue(false)
        error.postValue(false)
    }
}