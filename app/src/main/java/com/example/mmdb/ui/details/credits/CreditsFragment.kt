package com.example.mmdb.ui.details.credits

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mmdb.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jokubas.mmdb.model.data.dataclasses.Person
import kotlinx.android.synthetic.main.fragment_movie_credits.*
import kotlinx.android.synthetic.main.fragment_movie_credits.bottom_navigation
import kotlinx.android.synthetic.main.fragment_movie_credits.loading_error_text_view
import kotlinx.android.synthetic.main.fragment_movie_credits.progress_bar

class CreditsFragment : Fragment(), BottomNavigationView.OnNavigationItemSelectedListener {

    private val castAdapter = PeopleAdapter()
    private val crewAdapter = PeopleAdapter()
    private lateinit var viewModel: CreditsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_movie_credits, container, false)
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
        viewModel.credits?.observe(viewLifecycleOwner, Observer { credits ->
            credits?.let {
                updateCast(it.castList)
                updateCrew(it.crewList)
                loading_error_text_view.visibility = View.GONE
            } ?: run {
                loading_error_text_view.visibility = View.VISIBLE
            }
            progress_bar.visibility = View.GONE
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