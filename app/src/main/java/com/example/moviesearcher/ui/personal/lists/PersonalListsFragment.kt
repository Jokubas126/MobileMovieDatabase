package com.example.moviesearcher.ui.personal.lists

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviesearcher.R
import com.example.moviesearcher.model.data.LocalMovieList
import kotlinx.android.synthetic.main.fragment_personal_lists.*

class PersonalListsFragment : Fragment(), PersonalListsAdapter.ListOnClickListener {

    private lateinit var viewModel: PersonalListsViewModel

    private lateinit var listAdapter: PersonalListsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_personal_lists, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(PersonalListsViewModel::class.java)
        viewModel.fetch()

        recycler_view.layoutManager = LinearLayoutManager(context)

        observeViewModel()

        button_add_list.setOnClickListener{
            viewModel.showCreateListPopupWindow(context!!)
        }
    }

    private fun observeViewModel(){
        viewModel.movieLists.observe(viewLifecycleOwner, Observer {
            if (it != null){
                if (recycler_view.adapter == null){
                    listAdapter =
                        PersonalListsAdapter(
                            it,
                            this
                        )
                    recycler_view.adapter = listAdapter
                } else
                    listAdapter.updateMovieLists(it)
            }
        })

        viewModel.error.observe(viewLifecycleOwner, Observer { isError: Boolean? ->
            if (isError != null)
                loading_error_text_view!!.visibility =
                    if (isError)
                        View.VISIBLE
                    else View.GONE
        })
        viewModel.loading.observe(viewLifecycleOwner, Observer { isLoading: Boolean? ->
            if (isLoading != null) {
                progress_bar_loading_movie_list!!.visibility =
                    if (isLoading)
                        View.VISIBLE
                    else View.GONE
                if (isLoading)
                    loading_error_text_view!!.visibility = View.GONE
            }
        })
    }

    override fun onListClicked(view: View, list: LocalMovieList) {
        viewModel.onListClicked(view, list)
    }

    override fun onDeleteClicked(list: LocalMovieList) {
        viewModel.onDeleteListClicked(list)
    }
}
