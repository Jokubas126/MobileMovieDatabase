package com.example.mmdb.config

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class DrawerConfig {

    private val isDrawerEnabled = MutableLiveData(true)

    fun setDrawerEnabled(isEnabled: Boolean){
        isDrawerEnabled.postValue(isEnabled)
    }

    fun isDrawerEnabled(): Boolean = isDrawerEnabled.value ?: false

    fun isDrawerEnabledLiveData(): LiveData<Boolean> = isDrawerEnabled
}