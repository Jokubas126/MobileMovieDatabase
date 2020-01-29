package com.example.moviesearcher.view.details;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.moviesearcher.R;
import com.example.moviesearcher.model.util.ConverterUtil;
import com.example.moviesearcher.model.util.FragmentInflaterUtil;
import com.example.moviesearcher.model.util.JsonUtil;
import com.example.moviesearcher.viewmodel.MovieDetailsViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailsFragment extends Fragment implements BottomNavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.information_layout) RelativeLayout informationLayout;
    @BindView(R.id.movie_poster_view) ImageView moviePosterView;
    @BindView(R.id.backdrop_image_view) ImageView backdropImageView;
    @BindView(R.id.movie_title_view) TextView titleView;
    @BindView(R.id.movie_release_date_view) TextView yearView;
    @BindView(R.id.movie_countries_view) TextView countriesView;
    @BindView(R.id.movie_runtime_view) TextView runtimeView;
    @BindView(R.id.movie_score_view) TextView scoreView;
    @BindView(R.id.movie_genre_view) TextView genreView;
    @BindView(R.id.progress_bar_loading_details) ProgressBar progressBar;

    @BindView(R.id.details_bottom_navigation) BottomNavigationView bottomNavigationView;

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

        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        viewModel = ViewModelProviders.of(this).get(MovieDetailsViewModel.class);
        viewModel.fetch(getArguments());
        observeViewModel();
    }

    private void observeViewModel(){
        viewModel.getCurrentMovie().observe(this, movie -> {
                if (movie != null){
                    moviePosterView.setImageBitmap(ConverterUtil.HttpPathToBitmap(JsonUtil.getInstance().getPosterImageUrl(movie.getPosterImageUrl())));
                    backdropImageView.setImageBitmap(ConverterUtil.HttpPathToBitmap(JsonUtil.getInstance().getBackdropImageUrl(movie.getBackdropImageUrl())));
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

                    Bundle args = new Bundle();
                    args.putString("description", movie.getDescription());
                    FragmentInflaterUtil.replaceFragment(getFragmentManager(), new DescriptionFragment(),
                            R.id.movie_details_container, args);

                    informationLayout.setVisibility(View.VISIBLE);
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.description_menu_item:
                Log.d("DETAILS VIEW", "onNavigationItemSelected: DESCRIPTION SELECTED");
                Bundle argsDescr = new Bundle();
                argsDescr.putString("description", Objects.requireNonNull(viewModel.getCurrentMovie().getValue()).getDescription());
                FragmentInflaterUtil.replaceFragment(getFragmentManager(), new DescriptionFragment(),
                        R.id.movie_details_container, argsDescr);
                break;

            case R.id.videos_menu_item:
                Log.d("DETAILS VIEW", "onNavigationItemSelected: VIDEOS SELECTED");
                FragmentInflaterUtil.replaceFragment(getFragmentManager(), new VideosFragment(),
                        R.id.movie_details_container);
                break;

            case R.id.cast_menu_item:
                Log.d("DETAILS VIEW", "onNavigationItemSelected: CAST SELECTED");
                Bundle argsCast = new Bundle();
                argsCast.putInt("movieId", Objects.requireNonNull(viewModel.getCurrentMovie().getValue()).getId());
                FragmentInflaterUtil.replaceFragment(getFragmentManager(), new CastFragment(),
                        R.id.movie_details_container, argsCast);
                break;
        }

        return false;
    }
}
