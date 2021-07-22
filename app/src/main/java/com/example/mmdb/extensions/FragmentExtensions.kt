package com.example.mmdb.extensions

import androidx.fragment.app.Fragment
import com.example.mmdb.MainApplication
import com.example.mmdb.config.AppConfig
import com.example.mmdb.navigation.NavigationActivity
import com.example.mmdb.navigation.NavigationController

internal fun Fragment.requireAppConfig(): AppConfig = (requireActivity().application as MainApplication).config

internal fun Fragment.requireNavController(): NavigationController = (requireActivity() as NavigationActivity).navigationController