package com.jokubas.mmdb.moviedetails.credits

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.jokubas.mmdb.moviedetails.actions.InnerDetailsAction
import com.jokubas.mmdb.moviedetails.databinding.FragmentMovieCreditsBinding
import com.jokubas.mmdb.util.navigationtools.ConfigFragmentArgs
import com.jokubas.mmdb.util.navigationtools.action
import com.jokubas.mmdb.util.navigationtools.config

object CreditsFragmentArgs: ConfigFragmentArgs<InnerDetailsAction.Credits, CreditsConfig>()

class CreditsFragment : Fragment() {

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
}