package com.jokubas.mmdb.util.extensions

import androidx.databinding.BindingAdapter
import androidx.drawerlayout.widget.DrawerLayout

@BindingAdapter("lockMode")
fun DrawerLayout.setLockMode(isEnabled: Boolean) {
    if (isEnabled){
        setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
    } else {
        setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }
}