package com.example.mmdb.navigation

import android.os.Parcelable
import androidx.fragment.app.Fragment

open class BaseNavigationController {

    fun resolveFragment(action: Parcelable): Fragment? =
        actionRoutes.find { actionFragmentProviderPair ->
            actionFragmentProviderPair.key == action::class.java
        }?.let { providerPair ->
            providerPair.value as FragmentProvider<Parcelable>
        }?.let { fragmentProvider ->
            fragmentProvider(action)
        }
}