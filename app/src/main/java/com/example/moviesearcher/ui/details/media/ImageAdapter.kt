package com.example.moviesearcher.ui.details.media

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesearcher.R
import com.example.moviesearcher.databinding.ItemImageBinding
import com.example.moviesearcher.model.data.Image
import java.util.*

class ImageAdapter: RecyclerView.Adapter<ImageAdapter.ViewHolder>(){

    private val imagePathList: MutableList<Image> = ArrayList()

    fun updateImagePathList(list: List<Image>) {
        imagePathList.clear()
        imagePathList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: ItemImageBinding = DataBindingUtil.inflate(inflater, R.layout.item_image, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(imagePathList[position])
    }

    override fun getItemCount(): Int {
        return imagePathList.size
    }

    inner class ViewHolder(itemView: ItemImageBinding): RecyclerView.ViewHolder(itemView.root){
        private val view = itemView

        fun onBind(image: Image){
            view.image = image
        }
    }
}