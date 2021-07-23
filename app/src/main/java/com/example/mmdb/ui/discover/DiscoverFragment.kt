package com.example.mmdb.ui.discover

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mmdb.config.AppConfig
import com.example.mmdb.databinding.FragmentDiscoverBinding
import com.example.mmdb.extensions.requireAppConfig
import com.example.mmdb.extensions.requireNavController
import com.example.mmdb.navigation.*
import com.example.mmdb.navigation.actions.DiscoverFragmentAction

object DiscoverFragmentArgs: ConfigFragmentArgs<DiscoverFragmentAction, DiscoverFragmentConfig>()

class DiscoverFragment : Fragment() {

    private val appConfig: AppConfig by lazy {
        requireAppConfig()
    }

    private val navController: NavigationController by lazy {
        requireNavController()
    }

    private val config: DiscoverFragmentConfig by config()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appConfig.toolbarConfig.confirmButtonEnabled.set(true)
        appConfig.toolbarConfig.setBackFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentDiscoverBinding.inflate(inflater, container, false).apply {
            viewModel = ViewModelProvider(
                this@DiscoverFragment,
                DiscoverViewModelFactory(
                    config = config
                )
            ).get(DiscoverViewModel::class.java)
            lifecycleOwner = this@DiscoverFragment
        }.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        navController.detachFromNavigationController()
    }
}