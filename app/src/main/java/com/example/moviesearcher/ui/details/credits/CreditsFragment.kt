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

    private lateinit var crewLayout: LinearLayout
    private lateinit var castLayout: LinearLayout
    private lateinit var castView: RecyclerView
    private lateinit var crewView: RecyclerView
    private lateinit var bottomNavigationView: BottomNavigationView

    private val castAdapter = PeopleAdapter()
    private val crewAdapter = PeopleAdapter()
    private lateinit var viewModel: CreditsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_movie_cast, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(CreditsViewModel::class.java)
        viewModel.fetch(arguments)

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
        viewModel.credits?.observe(viewLifecycleOwner, Observer {
            if (it != null){
                updateCast(it.castList)
                updateCrew(it.crewList)
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
        return viewModel.onNavigationItemSelected(bottomNavigationView, menuItem)
    }

    private fun updateCast(castList: List<Person>?){
        if (castList != null) {
            castAdapter.updatePeopleList(castList)
            progress_bar.visibility = View.GONE
            castLayout.visibility = View.VISIBLE
        }
    }
    private fun updateCrew(crewList: List<Person>?){
        if (crewList != null) {
            crewAdapter.updatePeopleList(crewList)
            progress_bar.visibility = View.GONE
            crewLayout.visibility = View.VISIBLE
        }
    }
}