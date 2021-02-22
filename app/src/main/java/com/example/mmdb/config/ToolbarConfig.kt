package com.example.mmdb.config

import androidx.databinding.ObservableBoolean

class ToolbarConfig {

    val backButtonEnabled = ObservableBoolean(false)
    val drawerButtonEnabled = ObservableBoolean(false)

    val confirmButtonEnabled = ObservableBoolean(false)

    fun setBackFragment() {
        backButtonEnabled.set(true)
        drawerButtonEnabled.set(false)
    }

    fun setDrawerFragment() {
        backButtonEnabled.set(false)
        drawerButtonEnabled.set(true)
    }
}