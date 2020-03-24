package com.example.moviesearcher.util

import android.content.Context
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun showToast(context: Context, text: String, length: Int) {
    CoroutineScope(Dispatchers.Main).launch {
        Toast.makeText(context, text, length).show()
    }
}