package com.example.moviesearcher.ui.grid;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviesearcher.R;
import com.example.moviesearcher.databinding.ItemMovieBinding;
import com.example.moviesearcher.model.data.Movie;

import java.util.ArrayList;
import java.util.List;

class MainGridAdapter extends RecyclerView.Adapter<MainGridAdapter.ViewHolder> {

    private final List<Movie> movieList = new ArrayList<>();
    private MovieClickListener listener;

    MainGridAdapter(MovieClickListener listener) {
        this.listener = listener;
    }

    interface MovieClickListener {
        void onMovieClicked(View v);
    }

    void updateMovieList(List<Movie> movieList) {
        if (movieList.size() == 0)
            this.movieList.clear();
        if (this.movieList.containsAll(movieList))
            return;
        this.movieList.addAll(movieList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemMovieBinding view = DataBindingUtil.inflate(inflater, R.layout.item_movie, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemView.setMovie(movieList.get(position));
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ItemMovieBinding itemView;

        ViewHolder(@NonNull ItemMovieBinding itemView) {
            super(itemView.getRoot());
            this.itemView = itemView;

            itemView.getRoot().setOnClickListener(v -> listener.onMovieClicked(v));
        }
    }
}
