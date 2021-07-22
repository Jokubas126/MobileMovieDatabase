package com.example.mmdb.navigation

import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment

internal const val configProviderClassNameKey = "config_provider_class_name"
internal const val actionKey = "action"

interface ConfigProvider<T> {
    fun config(fragment: Fragment): T
}

fun <T> Fragment.config() = lazy<T> {
    requireArguments().getString(configProviderClassNameKey)?.let { configClassName ->
        (Class.forName(configClassName).newInstance() as ConfigProvider<T>).config(this)
    } ?: throw IllegalStateException("Missing config")
}

fun <T : Parcelable> Fragment.action() = lazy<T> {
    requireArguments().getParcelable(actionKey) ?: throw IllegalStateException("Missing action")
}

internal fun Bundle.action(action: Parcelable) {
    putParcelable(actionKey, action)
}

internal fun Bundle.configProvider(configProviderClass: Class<*>) {
    putString(configProviderClassNameKey, configProviderClass.name)
}

open class ConfigFragmentArgs<Action : Parcelable, Config> {
    fun create(
        action: Action,
        configProvider: Class<out ConfigProvider<Config>>
    ): Bundle {
        val arguments = Bundle()
        arguments.action(action)
        arguments.configProvider(configProvider)
        return arguments
    }
}

open class ConfigOnlyFragmentArgs<Config> {
    fun create(configProvider: Class<out ConfigProvider<Config>>): Bundle {
        val arguments = Bundle()
        arguments.configProvider(configProvider)
        return arguments
    }
}

open class ActionOnlyFragmentArgs<Action : Parcelable> {
    fun create(action: Action): Bundle {
        val arguments = Bundle()
        arguments.action(action)
        return arguments
    }
}