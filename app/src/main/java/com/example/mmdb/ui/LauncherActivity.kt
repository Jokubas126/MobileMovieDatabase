package com.example.mmdb.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.mmdb.R

class LauncherActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)

        val viewModel = ViewModelProvider(this).get(LauncherViewModel::class.java)
        viewModel.isLoaded.observe(this, Observer {
            it?.let {
                if (it) startActivity(Intent(this, MainActivity::class.java))
            }
        })
    }

    override fun onPause() {
        super.onPause()
        finish()
    }
}