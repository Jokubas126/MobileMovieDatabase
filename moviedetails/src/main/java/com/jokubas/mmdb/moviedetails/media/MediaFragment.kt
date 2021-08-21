package com.jokubas.mmdb.moviedetails.media

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.jokubas.mmdb.moviedetails.actions.InnerDetailsAction
import com.jokubas.mmdb.moviedetails.databinding.FragmentMovieMediaBinding
import com.jokubas.mmdb.util.navigationtools.ConfigFragmentArgs
import com.jokubas.mmdb.util.navigationtools.action
import com.jokubas.mmdb.util.navigationtools.config

object MediaFragmentArgs: ConfigFragmentArgs<InnerDetailsAction.Media, MediaConfig>()

class MediaFragment : Fragment() {

    private val action: InnerDetailsAction.Media by action()
    private val config: MediaConfig by config()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentMovieMediaBinding.inflate(inflater, container, false).apply {
        viewModel = ViewModelProvider(
            this@MediaFragment,
            MediaViewModelFactory(
                action = action,
                config = config
            )
        ).get(MediaViewModel::class.java)
        lifecycleOwner = this@MediaFragment
    }.root
}