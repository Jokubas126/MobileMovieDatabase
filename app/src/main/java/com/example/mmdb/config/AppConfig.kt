package com.example.mmdb.config

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.fragment.app.Fragment
import com.example.mmdb.MainApplication
import okhttp3.OkHttpClient

class AppConfig(context: Context) {

    val configVars: ConfigVars = ConfigVars()

    val movieConfig: MovieConfig by lazy {
        MovieConfig(
            movieServiceUrl = configVars.BASE_MOVIE_URL,
            httpClientBuilder = OkHttpClient.Builder()
        )
    }
}

fun Application.requireAppConfig(): AppConfig = (this as MainApplication).config
fun Activity.requireAppConfig(): AppConfig = (application as MainApplication).config
fun Fragment.requireAppConfig(): AppConfig = (requireActivity().application as MainApplication).config