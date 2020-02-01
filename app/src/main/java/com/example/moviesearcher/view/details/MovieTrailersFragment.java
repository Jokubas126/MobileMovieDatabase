package com.example.moviesearcher.view.details;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.moviesearcher.R;
import com.example.moviesearcher.util.BundleUtil;
import com.example.moviesearcher.viewmodel.MovieTrailersViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieTrailersFragment extends Fragment implements BottomNavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.information_layout) LinearLayout informationLayout;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.bottom_navigation) BottomNavigationView bottomNavigationView;

    private MovieTrailersViewModel viewModel;

    public MovieTrailersFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_trailers, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        viewModel = ViewModelProviders.of(this).get(MovieTrailersViewModel.class);
        viewModel.fetch(getArguments());

        observeViewModel();
    }

    private void observeViewModel(){
        viewModel.getLoading().observe(this, isLoading -> {
            if (isLoading != null){
                progressBar.setVisibility(isLoading? View.VISIBLE : View.GONE);
                informationLayout.setVisibility(isLoading? View.GONE : View.VISIBLE);
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.overview_menu_item:
                if (getArguments() != null) {
                    NavDirections action = MovieTrailersFragmentDirections.actionMovieOverview(getArguments().getInt(BundleUtil.KEY_MOVIE_ID));
                    Navigation.findNavController(bottomNavigationView).navigate(action);
                }
                break;

            case R.id.cast_menu_item:
                if (getArguments() != null) {
                    NavDirections action = MovieTrailersFragmentDirections.actionMovieCast(getArguments().getInt(BundleUtil.KEY_MOVIE_ID));
                    Navigation.findNavController(bottomNavigationView).navigate(action);
                }
                break;
        }

        return false;
    }
}
