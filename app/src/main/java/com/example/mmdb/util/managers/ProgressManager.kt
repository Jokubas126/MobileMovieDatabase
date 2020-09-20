package com.example.mmdb.util.managers

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

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