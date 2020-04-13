package com.example.moviesearcher.ui.details.credits

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesearcher.R
import com.example.moviesearcher.model.data.Person
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_movie_cast.*

class CreditsFragment : Fragment(), BottomNavigationView.OnNavigationItemSelectedListener {

    private val castAdapter = PeopleAdapter()
    private val crewAdapter = PeopleAdapter()
    private lateinit var viewModel: CreditsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_movie_cast, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(
            this,
            CreditsViewModelFactory(activity!!.application, arguments)
        ).get(CreditsViewModel::class.java)

        bottom_navigation.setOnNavigationItemSelectedListener(this)
        setupRecyclerViews()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.credits.observe(viewLifecycleOwner, Observer { credits ->
            credits?.let {
                updateCast(it.castList)
                updateCrew(it.crewList)
            }
        })
        viewModel.loading.observe(viewLifecycleOwner, Observer { isLoading ->
            isLoading?.let {
                progress_bar.visibility =
                    if (it) View.VISIBLE
                    else View.GONE
                if (it) {
                    cast_layout.visibility = View.GONE
                    crew_layout.visibility = View.GONE
                }
            }
        })
    }

    private fun updateCast(castList: List<Person>?) {
        castList?.let {
            castAdapter.updatePeopleList(it)
            progress_bar.visibility = View.GONE
            cast_layout.visibility = View.VISIBLE
        }
    }

    private fun updateCrew(crewList: List<Person>?) {
        crewList?.let {
            crewAdapter.updatePeopleList(it)
            progress_bar.visibility = View.GONE
            crew_layout.visibility = View.VISIBLE
        }
    }

    private fun setupRecyclerViews() {
        cast_recycler_view.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        cast_recycler_view.itemAnimator = DefaultItemAnimator()
        cast_recycler_view.adapter = castAdapter

        crew_recycler_view.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        crew_recycler_view.itemAnimator = DefaultItemAnimator()
        crew_recycler_view.adapter = crewAdapter
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        return viewModel.onNavigationItemSelected(bottom_navigation, menuItem)
    }
}