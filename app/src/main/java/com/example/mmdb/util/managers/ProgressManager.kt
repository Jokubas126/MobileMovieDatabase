package com.example.mmdb.util.managers

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

open class ProgressManager(

) : CoroutineScope {

    val loading = MutableLiveData<Boolean>()
    val error = MutableLiveData<Boolean>()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    open fun loading() {
        launch {
            loading.value = true
            error.value = false
        }
    }

    open fun load() {
        launch { loading.value = true }
    }

    open fun error() {
        launch {
            loading.value = false
            error.value = true
        }
    }

    open fun retrieved() {
        launch {
            loading.value = false
            error.value = false
        }
    }
}