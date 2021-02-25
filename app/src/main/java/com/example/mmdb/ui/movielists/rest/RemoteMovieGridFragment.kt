package com.example.mmdb.ui.movielists.rest

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.mmdb.databinding.FragmentMoviesGridBinding
import com.example.mmdb.extensions.requireAppConfig
import com.example.mmdb.navigation.ConfigFragmentArgs
import com.example.mmdb.navigation.action
import com.example.mmdb.navigation.actions.RemoteMovieGridFragmentAction
import com.example.mmdb.navigation.config
import kotlinx.android.synthetic.main.fragment_movies_grid.*

object RemoteMovieGridFragmentArgs : ConfigFragmentArgs<RemoteMovieGridFragmentAction, RemoteMovieGridFragmentConfig>()

class RemoteMovieGridFragment : Fragment() {

    private lateinit var viewModel: RemoteMovieGridViewModel

    private var layoutManager: StaggeredGridLayoutManager? = null
    private var state: Parcelable? = null

    private val action: RemoteMovieGridFragmentAction by action()
    private val config: RemoteMovieGridFragmentConfig by config()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMoviesGridBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(
            this,
            RemoteMovieGridViewModelFactory(
                requireActivity().application,
                requireAppConfig(),
                action,
                config
            )
        ).get(RemoteMovieGridViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        state?.let { layoutManager?.onRestoreInstanceState(it) }
        refresh_layout.setOnRefreshListener {
            viewModel.refresh()
            refresh_layout.isRefreshing = false
        }
    }

    override fun onPause() {
        super.onPause()
        state = movie_recycler_view.layoutManager?.onSaveInstanceState()
    }
}