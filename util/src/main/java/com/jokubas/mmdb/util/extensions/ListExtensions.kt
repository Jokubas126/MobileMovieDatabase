package com.jokubas.mmdb.util.extensions

import androidx.annotation.MainThread
import androidx.databinding.ObservableList

@MainThread
fun <T> ObservableList<T>.replaceAt(index: Int, item: T) {
    removeAt(index)
    add(index, item)
}