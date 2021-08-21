package com.example.mmdb.ui.movielists.moviegrid

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.mmdb.databinding.FragmentMoviesGridBinding
import com.jokubas.mmdb.util.navigationtools.ConfigFragmentArgs
import com.jokubas.mmdb.util.navigationtools.action
import com.example.mmdb.navigation.actions.MovieGridFragmentAction
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
