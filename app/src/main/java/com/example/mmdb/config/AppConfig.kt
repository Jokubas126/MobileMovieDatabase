package com.example.mmdb.config

import android.app.Activity
import android.app.Application
import com.example.mmdb.MainApplication
import okhttp3.OkHttpClient

class AppConfig(application: Application) {

    private val configVars: ConfigVars = ConfigVars()

    val movieConfig: MovieConfig by lazy {
        MovieConfig(
            application = application,
            movieServiceUrl = configVars.BASE_MOVIE_URL,
            httpClientBuilder = OkHttpClient.Builder()
        )
    }

    val toolbarConfig: ToolbarConfig by lazy {
        ToolbarConfig()
    }

    val drawerConfig: DrawerConfig by lazy {
        DrawerConfig()
    }
}

fun Application.requireAppConfig(): AppConfig = (this as MainApplication).config
fun Activity.requireAppConfig(): AppConfig = (application as MainApplication).config