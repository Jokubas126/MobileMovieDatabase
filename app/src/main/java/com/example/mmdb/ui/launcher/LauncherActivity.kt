package com.example.mmdb.ui.launcher

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.mmdb.R
import com.example.mmdb.config.requireAppConfig
import com.example.mmdb.ui.MainActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_launcher.*

class LauncherActivity : AppCompatActivity() {

    private lateinit var viewModel: LauncherViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)
        viewModel = ViewModelProvider(
            this,
            LauncherViewModelFactory(requireAppConfig())
        ).get(LauncherViewModel::class.java)
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.loadedEvent.observe(this, { isLoaded ->
            isLoaded?.let {
                startActivity(Intent(this, MainActivity::class.java))
            }
        })
        viewModel.updateRequiredEvent.observe(this, { isUpdateRequired ->
            isUpdateRequired?.let {
                Snackbar.make(launcher_layout, R.string.update_required, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.try_again) {
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