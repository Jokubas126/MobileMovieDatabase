package com.example.mmdb.ui.details.innerdetails.credits

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mmdb.databinding.FragmentMovieCreditsBinding
import com.example.mmdb.extensions.requireNavController
import com.example.mmdb.navigation.ConfigFragmentArgs
import com.example.mmdb.navigation.action
import com.example.mmdb.navigation.actions.InnerDetailsAction
import com.example.mmdb.navigation.config

object CreditsFragmentArgs: ConfigFragmentArgs<InnerDetailsAction.Credits, CreditsConfig>()

class CreditsFragment : Fragment() {

    private val navController by lazy {
        requireNavController()
    }

    private val action: InnerDetailsAction.Credits by action()
    private val config: CreditsConfig by config()

    private val creditsViewModel: CreditsViewModel by lazy {
        ViewModelProvider(
            this,
            CreditsViewModelFactory(
                action = action,
                config = config
            )
        ).get(CreditsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentMovieCreditsBinding.inflate(inflater, container, false).apply {
        lifecycleOwner = viewLifecycleOwner
        viewModel = creditsViewModel
    }.root


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController.detailsNavigationController.attachToNavigationController(childFragmentManager)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        navController.detailsNavigationController.detachFromNavigationController()
    }
}