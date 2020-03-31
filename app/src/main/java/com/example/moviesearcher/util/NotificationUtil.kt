package com.example.moviesearcher.util

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun showToast(context: Context, text: String, length: Int) {
    CoroutineScope(Dispatchers.Main).launch {
        Toast.makeText(context, text, length).show()
    }
}

fun showProgressSnackBar(root: View, text: String) {
    CoroutineScope(Dispatchers.Main).launch {
        val snackbar = Snackbar.make(root, text, Snackbar.LENGTH_INDEFINITE)
        val barLayout: ViewGroup =
            (snackbar.view.findViewById(com.google.android.material.R.id.snackbar_text) as View).parent as ViewGroup
        val progressBar = ProgressBar(snackbar.context)
        barLayout.addView(progressBar)
        snackbar.show()
    }
}