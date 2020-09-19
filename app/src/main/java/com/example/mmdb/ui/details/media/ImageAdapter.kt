package com.example.mmdb.ui.details.media

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mmdb.R
import com.example.mmdb.databinding.ItemImageBinding
import com.jokubas.mmdb.model.data.dataclasses.Image
import java.util.*

class ImageAdapter: RecyclerView.Adapter<ImageAdapter.ViewHolder>(){

    private val imageList: MutableList<Image> = ArrayList()

    fun updateImageList(list: List<Image>) {
        imageList.clear()
        imageList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: ItemImageBinding = DataBindingUtil.inflate(inflater, R.layout.item_image, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(imageList[position])
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    inner class ViewHolder(itemView: ItemImageBinding): RecyclerView.ViewHolder(itemView.root){
        private val view = itemView

        fun onBind(image: Image){
            view.image = image
        }
    }
}