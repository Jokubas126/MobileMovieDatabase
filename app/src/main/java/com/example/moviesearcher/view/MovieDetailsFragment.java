package com.example.moviesearcher.view;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.moviesearcher.R;
import com.example.moviesearcher.viewmodel.MovieDetailsViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailsFragment extends Fragment {

    @BindView(R.id.information_layout) LinearLayout informationLayout;
    @BindView(R.id.movie_poster_view) ImageView moviePosterView;
    @BindView(R.id.backdrop_image_view) ImageView backdropImageView;
    @BindView(R.id.movie_title_view) TextView titleView;
    @BindView(R.id.movie_release_date_view) TextView yearView;
    @BindView(R.id.movie_countries_view) TextView countriesView;
    @BindView(R.id.movie_runtime_view) TextView runtimeView;
    @BindView(R.id.movie_score_view) TextView scoreView;
    @BindView(R.id.movie_genre_view) TextView genreView;
    @BindView(R.id.movie_description_view) TextView descriptionView;
    @BindView(R.id.progress_bar_loading_details) ProgressBar progressBar;

    private MovieDetailsViewModel viewModel;

    public MovieDetailsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_details, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = ViewModelProviders.of(this).get(MovieDetailsViewModel.class);
        viewModel.fetch(getArguments());
        observeViewModel();
    }

    private void observeViewModel(){
        viewModel.getCurrentMovie().observe(this, movie -> {
                if (movie != null){
                    informationLayout.setVisibility(View.VISIBLE);
                    backdropImageView.setImageBitmap(movie.getBackdropImage());
                    moviePosterView.setImageBitmap(movie.getPosterImage());
                    titleView.setText(movie.getTitle());
                    yearView.setText(movie.getReleaseDate());
                    for(String country : movie.getProductionCountries()) {
                        if (countriesView.getText() != null && !countriesView.getText().toString().equals("")) {
                            String countryText = countriesView.getText().toString() + ", " + country;
                            countriesView.setText(countryText);
                        } else {
                            countriesView.setText(country);
                        }
                    }
                    String runtimeText = movie.getRuntime() + getResources().getString(R.string.min_ending);
                    runtimeView.setText(runtimeText);
                    scoreView.setText(movie.getScore());
                    for(String genre : movie.getGenres()){
                        if (genreView.getText() != null && !genreView.getText().toString().equals("")){
                            String genreText = genreView.getText().toString() + ", " + genre;
                            genreView.setText(genreText);
                        } else {
                            genreView.setText(genre);
                        }
                    }
                    descriptionView.setText(movie.getDescription());
                }
        });

        viewModel.getLoading().observe(this, isLoading -> {
            if (isLoading != null){
                progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
                if(isLoading)
                    informationLayout.setVisibility(View.GONE);
            }
        });
    }
}
