package com.jokubas.mmdb.moviedetails.ui.overview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.jokubas.mmdb.moviedetails.actions.InnerDetailsAction
import com.jokubas.mmdb.moviedetails.databinding.FragmentMovieOverviewBinding
import com.jokubas.mmdb.util.navigationtools.ConfigFragmentArgs
import com.jokubas.mmdb.util.navigationtools.action
import com.jokubas.mmdb.util.navigationtools.config

object OverviewFragmentArgs : ConfigFragmentArgs<InnerDetailsAction.Overview, OverviewConfig>()

class OverviewFragment : Fragment() {

    private val action: InnerDetailsAction.Overview by action()
    private val config: OverviewConfig by config()

    private val overviewViewModel: OverviewViewModel by lazy {
        ViewModelProvider(
            this,
            OverviewViewModelFactory(
                action = action,
                config = config
            )
        ).get(OverviewViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentMovieOverviewBinding.inflate(inflater, container, false).apply {
        viewModel = overviewViewModel
        lifecycleOwner = this@OverviewFragment
    }.root
}