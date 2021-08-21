package com.jokubas.mmdb.moviegrid.ui.grid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.jokubas.mmdb.moviegrid.actions.MovieGridFragmentAction
import com.jokubas.mmdb.moviegrid.databinding.FragmentMoviesGridBinding
import com.jokubas.mmdb.util.navigationtools.ConfigFragmentArgs
import com.jokubas.mmdb.util.navigationtools.action
import com.jokubas.mmdb.util.navigationtools.config

object MovieGridFragmentArgs : ConfigFragmentArgs<MovieGridFragmentAction, MovieGridFragmentConfig>()

class MovieGridFragment : Fragment() {

    private val action: MovieGridFragmentAction by action()
    private val config: MovieGridFragmentConfig by config()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentMoviesGridBinding.inflate(
            LayoutInflater.from(context),
            container,
            false
        ).apply {
            viewModel = ViewModelProvider(
                this@MovieGridFragment,
                MovieGridViewModelFactory(action, config)
            ).get(MovieGridViewModel::class.java)
        }.root
    }
}
