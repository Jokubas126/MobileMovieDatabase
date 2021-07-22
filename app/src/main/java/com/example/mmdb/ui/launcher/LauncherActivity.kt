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

class LauncherActivity : AppCompatActivity(R.layout.activity_launcher) {

    private val viewModel: LauncherViewModel by lazy {
        ViewModelProvider(
            this,
            LauncherViewModelFactory(requireAppConfig())
        ).get(LauncherViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.loadedEvent.observe(this, { isLoaded ->
            isLoaded?.let {
                startActivity(Intent(this, MainActivity::class.java))
            }
        })
        viewModel.updateRequiredEvent.observe(this, { isUpdateRequired ->
            isUpdateRequired?.let { updateEvent ->
                Snackbar.make(launcher_layout, R.string.update_required, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.try_again) {
                        updateEvent.update.invoke()
                    }
                    .show()
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        finish()
    }
}