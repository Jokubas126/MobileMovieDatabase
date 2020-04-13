package com.example.moviesearcher.ui.popup_windows

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesearcher.R
import com.example.moviesearcher.databinding.ItemCustomListToAddBinding
import com.example.moviesearcher.model.data.CustomMovieList
import com.example.moviesearcher.model.data.Movie
import kotlinx.android.synthetic.main.item_custom_list_to_add.view.*
import kotlinx.android.synthetic.main.popup_window_personal_lists_to_add.view.*

class PersonalListsPopupWindow(
    private val root: View,
    private val view: View,
    width: Int, height: Int,
    private val selectedMovie: Movie,
    private val listener: ListsConfirmedClickListener
) : PopupWindow(view, width, height, true), PersonalListsAdapter.ListCheckedListener {

    private val checkedLists = mutableListOf<CustomMovieList>()

    interface ListsConfirmedClickListener{
        fun onConfirmClicked(movie: Movie, checkedLists: List<CustomMovieList>, root: View): Boolean
    }

    init {
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        showAtLocation(view, Gravity.CENTER, 0, 0)

        view.popup_window_outside.setOnClickListener { dismiss() }

        view.confirm_btn.setOnClickListener {
            if (listener.onConfirmClicked(selectedMovie, checkedLists, root))
                dismiss()
        }
    }

    fun setupLists(movieLists: List<CustomMovieList>){
        val recyclerView = view.personal_lists_recycler_view
        recyclerView.layoutManager = LinearLayoutManager(root.context)
        recyclerView.addItemDecoration(DividerItemDecoration(root.context, DividerItemDecoration.VERTICAL))
        recyclerView.adapter = PersonalListsAdapter(this, movieLists)
    }

    override fun onListChecked(movieList: CustomMovieList, isChecked: Boolean) {
        if (isChecked) checkedLists.add(movieList)
        else checkedLists.remove(movieList)
    }
}

class PersonalListsAdapter(private val listener: ListCheckedListener, private val movieLists: List<CustomMovieList>): RecyclerView.Adapter<PersonalListsAdapter.ViewHolder>() {

    interface ListCheckedListener{
        fun onListChecked(movieList: CustomMovieList, isChecked: Boolean)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: ItemCustomListToAddBinding = DataBindingUtil.inflate(inflater, R.layout.item_custom_list_to_add, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(listener, movieLists[position])
    }

    override fun getItemCount(): Int {
        return movieLists.size
    }

    inner class ViewHolder(itemView: ItemCustomListToAddBinding): RecyclerView.ViewHolder(itemView.root){
        private val view = itemView

        fun onBind(listener: ListCheckedListener, movieList: CustomMovieList){
            view.movieList = movieList
            view.root.information_layout.setOnClickListener {
                view.root.list_checkbox.isChecked = !view.root.list_checkbox.isChecked
                listener.onListChecked(movieList, view.root.list_checkbox.isChecked)
            }
            view.root.list_checkbox.setOnCheckedChangeListener { _, isChecked -> listener.onListChecked(movieList, isChecked) }
        }
    }
}