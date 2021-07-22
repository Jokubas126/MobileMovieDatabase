package com.example.mmdb.ui.details.innerdetails.overview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mmdb.databinding.FragmentMovieOverviewBinding
import com.example.mmdb.navigation.ConfigFragmentArgs
import com.example.mmdb.navigation.action
import com.example.mmdb.navigation.actions.InnerDetailsAction
import com.example.mmdb.navigation.config
import kotlinx.android.synthetic.main.fragment_movie_overview.*

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