package com.example.moviesearcher.view.grid;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviesearcher.R;
import com.example.moviesearcher.databinding.ItemMovieBinding;
import com.example.moviesearcher.model.data.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieGridAdapter extends RecyclerView.Adapter<MovieGridAdapter.ViewHolder> implements MovieClickListener{

    private final List<Movie> movieList = new ArrayList<>();

    void updateMovieList(List<Movie> movieList){
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
        holder.itemView.setListener(this);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    @Override
    public void onMovieClicked(View v) {
        int movieId = Integer.parseInt(
                ((TextView) v.findViewById(R.id.movie_id)).getText().toString());
        NavDirections action = MoviesGridFragmentDirections.actionMovieDetails(movieId);
        Navigation.findNavController(v).navigate(action);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ItemMovieBinding itemView;

        ViewHolder(@NonNull ItemMovieBinding itemView) {
            super(itemView.getRoot());
            this.itemView = itemView;
        }
    }
}
