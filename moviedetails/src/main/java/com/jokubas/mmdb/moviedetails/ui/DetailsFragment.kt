package com.jokubas.mmdb.moviedetails.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.jokubas.mmdb.moviedetails.actions.DetailsFragmentAction
import com.jokubas.mmdb.moviedetails.databinding.FragmentDetailsWrapperBinding
import com.jokubas.mmdb.util.navigationtools.ConfigFragmentArgs
import com.jokubas.mmdb.util.navigationtools.action
import com.jokubas.mmdb.util.navigationtools.config

object DetailsFragmentArgs : ConfigFragmentArgs<DetailsFragmentAction, DetailsFragmentConfig>()

class DetailsFragment : Fragment() {

    private val action: DetailsFragmentAction by action()
    private val config: DetailsFragmentConfig by config()

    private val detailsViewModel: DetailsViewModel by lazy {
        ViewModelProvider(
            this,
            DetailsViewModelFactory(
                action = action,
                config = config
            )
        ).get(DetailsViewModel::class.java)
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

        config.loadInitialView.invoke(action.movieId, action.isRemote)
    }
}