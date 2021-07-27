package com.example.mmdb.ui.movielists.customlists

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
import com.jokubas.mmdb.util.SNACKBAR_LENGTH_LONG_MS
import com.google.android.material.snackbar.Snackbar
import com.jokubas.mmdb.model.data.entities.CustomMovieList

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

    }

    private fun observeViewModel() {
        viewModel.movieLists.observe(viewLifecycleOwner, Observer {
            listAdapter.updateMovieLists(it)
        })
    }

    private fun setupRecyclerView() {
        context?.let {
            listAdapter = CustomListsAdapter(it)
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

    override fun onDeleteClicked(view: View, list: CustomMovieList, position: Int) {
        listAdapter.movieLists.removeAt(position)
        listAdapter.notifyItemRemoved(position)
        var restored = false
        Snackbar.make(view, R.string.movie_list_deleted, Snackbar.LENGTH_LONG)
            .setAction(R.string.undo) {
                restored = true
                listAdapter.movieLists.add(position, list)
                listAdapter.notifyItemInserted(position)
            }.show()

        Handler().postDelayed({
            if (!restored)
                viewModel.deleteList(list)
        }, SNACKBAR_LENGTH_LONG_MS.toLong())
    }
}
