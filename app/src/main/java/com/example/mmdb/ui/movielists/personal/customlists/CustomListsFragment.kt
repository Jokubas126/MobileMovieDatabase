package com.example.mmdb.ui.movielists.personal.customlists

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mmdb.R
import com.example.mmdb.model.data.CustomMovieList
import com.example.mmdb.util.SNACKBAR_LENGTH_LONG_MS
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_custom_lists.*

class CustomListsFragment : Fragment(), CustomListsAdapter.ListOnClickListener,
    CustomListsAdapter.ListOptionsListener {

    private lateinit var viewModel: CustomListsViewModel

    private lateinit var listAdapter: CustomListsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_custom_lists, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(CustomListsViewModel::class.java)

        setupRecyclerView()

        observeViewModel()

        button_add_list.setOnClickListener {
            viewModel.showCreateListPopupWindow(context!!)
        }
    }

    private fun observeViewModel() {
        viewModel.movieLists.observe(viewLifecycleOwner, Observer {
            if (!it.isNullOrEmpty()) {
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

    private fun setupRecyclerView() {
        context?.let {
            listAdapter = CustomListsAdapter(it)
            recycler_view.layoutManager = LinearLayoutManager(it)
            recycler_view.adapter = listAdapter
            listAdapter.setListOnClickListener(this)
            listAdapter.setListOptionsListener(this)
        }
    }

    override fun onListClicked(view: View, list: CustomMovieList) {
        viewModel.onListClicked(view, list)
    }

    override fun onEditListTitle(list: CustomMovieList) {
        viewModel.updateMovieList(list)
    }

    override fun onDeleteClicked(view: View, list: CustomMovieList) {
        val oldList = mutableListOf<CustomMovieList>()
        val newList = mutableListOf<CustomMovieList>()
        oldList.addAll(listAdapter.movieLists)
        newList.addAll(oldList)
        newList.remove(list)
        listAdapter.updateMovieLists(newList)
        var restored = false
        Snackbar.make(view, R.string.movie_list_deleted, Snackbar.LENGTH_LONG)
            .setAction(R.string.undo) {
                restored = true
                listAdapter.updateMovieLists(oldList)
            }.show()

        Handler().postDelayed({
            if (!restored)
                viewModel.deleteList(list)
        }, SNACKBAR_LENGTH_LONG_MS.toLong())
    }
}
