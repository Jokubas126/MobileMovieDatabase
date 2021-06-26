package com.example.mmdb.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mmdb.config.AppConfig
import com.example.mmdb.databinding.FragmentDetailsWrapperBinding
import com.example.mmdb.extensions.requireAppConfig
import com.example.mmdb.extensions.requireNavController
import com.example.mmdb.navigation.ConfigFragmentArgs
import com.example.mmdb.navigation.NavigationController
import com.example.mmdb.navigation.action
import com.example.mmdb.navigation.actions.DetailsFragmentAction
import com.example.mmdb.navigation.config
import com.example.mmdb.ui.ToolbarViewModel

object DetailsFragmentArgs : ConfigFragmentArgs<DetailsFragmentAction, DetailsFragmentConfig>()

class DetailsFragment : Fragment() {

    private val appConfig: AppConfig by lazy {
        requireAppConfig()
    }

    private val navController: NavigationController by lazy {
        requireNavController()
    }

    private val action: DetailsFragmentAction by action()
    private val config: DetailsFragmentConfig by config()

    private val detailsViewModel: DetailsViewModel by lazy {
        ViewModelProvider(
            this,
            DetailsViewModelFactory(
                action = action,
                config = config,
                toolbarViewModel = ToolbarViewModel(
                    toolbarConfig = appConfig.toolbarConfig,
                    navController = navController
                )
            )
        ).get(DetailsViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appConfig.toolbarConfig.setBackFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentDetailsWrapperBinding.inflate(inflater, container, false).apply {
        viewModel = detailsViewModel
        lifecycleOwner = this@DetailsFragment
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        config.loadInitialView.invoke(action.idWrapper)
    }
}