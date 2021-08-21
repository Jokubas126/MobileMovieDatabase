package com.example.mmdb.ui.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mmdb.config.requireAppConfig
import com.example.mmdb.databinding.FragmentAboutBinding
import com.jokubas.mmdb.util.navigationtools.ConfigFragmentArgs
import com.example.mmdb.navigation.actions.AboutFragmentAction
import com.example.mmdb.navigation.requireNavController
import com.example.mmdb.ui.ToolbarViewModel

object AboutFragmentArgs : ConfigFragmentArgs<AboutFragmentAction, AboutFragmentConfig>()

class AboutFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        FragmentAboutBinding.inflate(inflater, container, false).apply {
            toolbarViewModel = ToolbarViewModel(
                toolbarConfig = requireAppConfig().toolbarConfig.apply { setBackFragment() },
                navController = requireNavController()
            )
            lifecycleOwner = this@AboutFragment
        }.root
}