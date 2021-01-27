package com.example.mmdb

import android.app.Application
import com.example.mmdb.config.AppConfig

class MainApplication : Application() {

    lateinit var config: AppConfig

    override fun onCreate() {
        super.onCreate()

        config = AppConfig(this)
    }
}