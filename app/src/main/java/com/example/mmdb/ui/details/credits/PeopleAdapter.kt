package com.example.mmdb.ui.details.credits

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mmdb.R
import com.example.mmdb.databinding.ItemPersonBinding
import com.example.mmdb.model.data.Person
import java.util.*

class PeopleAdapter : RecyclerView.Adapter<PeopleAdapter.ViewHolder>() {

    private val people: MutableList<Person> = ArrayList()

    fun updatePeopleList(list: List<Person>) {
        people.clear()
        people.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: ItemPersonBinding =
            DataBindingUtil.inflate(inflater, R.layout.item_person, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(people[position])
    }

    override fun getItemCount(): Int {
        return people.size
    }

    inner class ViewHolder(itemView: ItemPersonBinding) : RecyclerView.ViewHolder(itemView.root) {
        private val view = itemView

        fun onBind(person: Person) {
            view.person = person
        }
    }
}