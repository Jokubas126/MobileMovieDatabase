package com.example.mmdb.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.mmdb.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_launcher.*

class LauncherActivity : AppCompatActivity() {

    private lateinit var viewModel: LauncherViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)
        viewModel = ViewModelProvider(this).get(LauncherViewModel::class.java)
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.isLoaded.observe(this, Observer { isLoaded ->
            isLoaded?.let {
                if (it) startActivity(Intent(this, MainActivity::class.java))
            }
        })
        viewModel.isUpdateRequired.observe(this, Observer { isUpdateRequired ->
            isUpdateRequired?.let {
                if (it)
                    Snackbar.make(launcher_layout, "Data update required. Connect to internet and try again", Snackbar.LENGTH_INDEFINITE)
                        .setAction("TRY AGAIN") {
                            viewModel.updateApplication()
                        }
                        .show()
            }
        })
    }

    override fun onPause() {
        super.onPause()
        finish()
    }
}