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
import com.example.mmdb.navigation.ConfigFragmentArgs
import com.example.mmdb.navigation.action
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
                activity!!.application,
                action,
                config
            ) { movieRemoteId ->
                /*val action =
                    RestMovieGridFragmentDirections.actionMovieDetails()
                action.movieRemoteId = movieRemoteId
                findNavController().navigate(action)*/
            }
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

    private fun setupTitle() {
        /*arguments?.let { arguments ->
            val args = RestMovieGridFragmentArgs.fromBundle(arguments)
            val title =
                when (args.movieGridType) {
                    TYPE_MOVIE_LIST ->
                        args.keyCategory?.let {
                            val title = StringBuilder()
                            val array = it.split("_").toTypedArray()
                            for (stringPart in array)
                                title.append(stringPart.capitalize(Locale.getDefault())).append(" ")
                            title.append("Movies")
                        } ?: run {
                            KEY_POPULAR.capitalize(Locale.ROOT) + " Movies"
                        }
                    SEARCH_MOVIE_LIST -> args.searchQuery
                    DISCOVER_MOVIE_LIST -> args.discoverNameArray?.let {
                        stringListToString(it.toList())
                    }
                    else -> ""
                }
            (activity as AppCompatActivity).supportActionBar?.title = title.toString()
        }*/
    }

    override fun onResume() {
        super.onResume()
        setupTitle()
    }

    override fun onPause() {
        super.onPause()
        state = movie_recycler_view.layoutManager?.onSaveInstanceState()
    }
}