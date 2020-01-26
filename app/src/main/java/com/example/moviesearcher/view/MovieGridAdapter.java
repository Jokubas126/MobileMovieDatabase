package com.example.moviesearcher.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviesearcher.R;
import com.example.moviesearcher.model.Movie;

import java.util.List;

public class MovieGridAdapter extends RecyclerView.Adapter<MovieGridAdapter.ViewHolder>{

    private final List<Movie> movieList;

    MovieGridAdapter(List<Movie> movieList) {
        this.movieList = movieList;
    }

    void updateMovieList(List<Movie> movieList){
        this.movieList.clear();
        this.movieList.addAll(movieList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.titleView.setText(movieList.get(position).getTitle());
        holder.releaseDateView.setText(movieList.get(position).getReleaseDate());
        holder.IMDbScoreView.setText(movieList.get(position).getIMDbScore());
        holder.movieCoverImage.setImageBitmap(movieList.get(position).getCoverImage());
        StringBuilder genreString = null;
        for (String genre : movieList.get(position).getGenres()){
            if (genreString != null){
                genreString.append(", ").append(genre);
            } else {
                genreString = genre == null ? null : new StringBuilder(genre);
            }
        }
        holder.genresView.setText(genreString == null ? null : genreString.toString());
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        View itemView;
        private ImageView movieCoverImage;
        private TextView titleView;
        private TextView releaseDateView;
        private TextView IMDbScoreView;
        private TextView genresView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            movieCoverImage = itemView.findViewById(R.id.movie_image_view);
            titleView = itemView.findViewById(R.id.movie_title_view);
            releaseDateView = itemView.findViewById(R.id.movie_release_date_view);
            IMDbScoreView = itemView.findViewById(R.id.movie_score_view);
            genresView = itemView.findViewById(R.id.movie_genre_view);
        }
    }
}
