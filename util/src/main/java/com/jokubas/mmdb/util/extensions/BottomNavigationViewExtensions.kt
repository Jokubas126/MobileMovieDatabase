package com.jokubas.mmdb.util.extensions

import android.view.MenuItem
import androidx.databinding.BindingAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView

@BindingAdapter("onItemSelected")
fun BottomNavigationView.onItemSelected(
    onMenuItemSelected: (menuItem: MenuItem) -> Unit
) {
    setOnNavigationItemSelectedListener {
        onMenuItemSelected.invoke(it)
        true
    }
}