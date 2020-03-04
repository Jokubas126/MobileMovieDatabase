package com.example.moviesearcher.ui.details.cast

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesearcher.R
import com.example.moviesearcher.model.data.Person
import com.example.moviesearcher.util.BundleUtil
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_movie_cast.*

class CastFragment : Fragment(), BottomNavigationView.OnNavigationItemSelectedListener {

    private lateinit var crewLayout: LinearLayout
    private lateinit var castLayout: LinearLayout
    private lateinit var castView: RecyclerView
    private lateinit var crewView: RecyclerView
    private lateinit var bottomNavigationView: BottomNavigationView

    private val castAdapter = PeopleAdapter()
    private val crewAdapter = PeopleAdapter()
    private lateinit var viewModel: CastViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_movie_cast, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CastViewModel::class.java)
        viewModel.fetch(activity as Activity, arguments)

        crewLayout = crew_layout
        castLayout = cast_layout
        castView = cast_recycler_view
        crewView = crew_recycler_view
        bottomNavigationView = bottom_navigation

        bottomNavigationView.setOnNavigationItemSelectedListener(this)
        castView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        castView.itemAnimator = DefaultItemAnimator()
        castView.adapter = castAdapter
        crewView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        crewView.itemAnimator = DefaultItemAnimator()
        crewView.adapter = crewAdapter
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.cast.observe(viewLifecycleOwner, Observer { cast: List<Person>? ->
            if (cast != null) {
                castLayout.visibility = View.VISIBLE
                progress_bar.visibility = View.GONE
                castAdapter.updatePeopleList(cast)
            }
        })
        viewModel.crew.observe(viewLifecycleOwner, Observer { crew: List<Person>? ->
            if (crew != null) {
                crewLayout.visibility = View.VISIBLE
                progress_bar.visibility = View.GONE
                crewAdapter.updatePeopleList(crew)
            }
        })
        viewModel.loading.observe(viewLifecycleOwner, Observer { isLoading: Boolean? ->
            if (isLoading != null) {
                progress_bar.visibility =
                        if (isLoading)
                            View.VISIBLE
                        else View.GONE
                if (isLoading) {
                    castLayout.visibility = View.GONE
                    crewLayout.visibility = View.GONE
                }
            }
        })
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.media_menu_item -> if (arguments != null) {
                val action: NavDirections = CastFragmentDirections.actionMovieMedia(arguments!!.getInt(BundleUtil.KEY_MOVIE_ID))
                Navigation.findNavController(bottomNavigationView).navigate(action)
            }
            R.id.overview_menu_item -> if (arguments != null) {
                val action: NavDirections = CastFragmentDirections.actionMovieOverview(arguments!!.getInt(BundleUtil.KEY_MOVIE_ID))
                Navigation.findNavController(bottomNavigationView).navigate(action)
            }
        }
        return false
    }

}