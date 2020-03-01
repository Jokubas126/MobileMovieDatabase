package com.example.moviesearcher.ui.details.overview;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.moviesearcher.R;
import com.example.moviesearcher.databinding.FragmentMovieOverviewBinding;
import com.example.moviesearcher.util.BundleUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OverviewFragment extends Fragment implements BottomNavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.information_layout) ConstraintLayout informationLayout;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.bottom_navigation) BottomNavigationView bottomNavigationView;

    private FragmentMovieOverviewBinding fragmentView;
    private OverviewViewModel viewModel;

    public OverviewFragment(){}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentView = DataBindingUtil.inflate(inflater, R.layout.fragment_movie_overview, container,false);
        ButterKnife.bind(this, fragmentView.getRoot());
        return fragmentView.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        viewModel = ViewModelProviders.of(this).get(OverviewViewModel.class);
        viewModel.fetch(getActivity(), getArguments());
        observeViewModel();
    }

    private void observeViewModel(){
        viewModel.getCurrentMovie().observe(getViewLifecycleOwner(), movie -> {
            if (movie != null){
                fragmentView.setMovie(movie);
                informationLayout.setVisibility(View.VISIBLE);
            }
        });

        viewModel.getLoading().observe(getViewLifecycleOwner(), isLoading -> {
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
            case R.id.media_menu_item:
                if (getArguments() != null) {
                    NavDirections action = OverviewFragmentDirections.actionMovieMedia(getArguments().getInt(BundleUtil.KEY_MOVIE_ID));
                    Navigation.findNavController(bottomNavigationView).navigate(action);
                }
                break;

            case R.id.cast_menu_item:
                if (getArguments() != null) {
                    NavDirections action = OverviewFragmentDirections.actionMovieCast(getArguments().getInt(BundleUtil.KEY_MOVIE_ID));
                    Navigation.findNavController(bottomNavigationView).navigate(action);
                }
                break;
        }
        return false;
    }

}
