package com.jokubas.mmdb.moviediscover.ui.discover

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.jokubas.mmdb.moviediscover.actions.DiscoverFragmentAction
import com.jokubas.mmdb.moviediscover.databinding.FragmentDiscoverBinding
import com.jokubas.mmdb.util.navigationtools.ConfigFragmentArgs
import com.jokubas.mmdb.util.navigationtools.config

object DiscoverFragmentArgs : ConfigFragmentArgs<DiscoverFragmentAction, DiscoverFragmentConfig>()

class DiscoverFragment : Fragment() {

    private val config: DiscoverFragmentConfig by config()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentDiscoverBinding.inflate(inflater, container, false).apply {
        viewModel = ViewModelProvider(
            this@DiscoverFragment,
            DiscoverViewModelFactory(
                config = config
            )
        ).get(DiscoverViewModel::class.java)
        lifecycleOwner = this@DiscoverFragment
    }.root
}