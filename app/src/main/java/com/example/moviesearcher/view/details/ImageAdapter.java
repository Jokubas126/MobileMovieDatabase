package com.example.moviesearcher.view.details;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviesearcher.R;
import com.example.moviesearcher.databinding.ItemImageBinding;

import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private List<String> imagePathList = new ArrayList<>();

    public void updateImagePathList(List<String> list){
        imagePathList.clear();
        imagePathList.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemImageBinding view = DataBindingUtil.inflate(inflater, R.layout.item_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemView.setImagePath(imagePathList.get(position));
    }

    @Override
    public int getItemCount() {
        return imagePathList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private ItemImageBinding itemView;

        ViewHolder(@NonNull ItemImageBinding itemView) {
            super(itemView.getRoot());
            this.itemView = itemView;
        }
    }
}
