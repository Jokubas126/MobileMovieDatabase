package com.example.moviesearcher.view;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.moviesearcher.R;
import com.example.moviesearcher.model.Movie;
import com.example.moviesearcher.model.handlers.JsonMovieHandler;
import com.example.moviesearcher.model.handlers.MovieListAsyncResponse;
import com.example.moviesearcher.viewmodel.MovieGridViewModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoviesGridFragment extends Fragment {

    private MovieGridViewModel viewModel;
    private MovieGridAdapter gridAdapter = new MovieGridAdapter(new ArrayList<>());

    @BindView(R.id.movie_grid_view)
    RecyclerView recyclerView;

    @BindView(R.id.loading_error_text_view)
    TextView errorTextView;

    @BindView(R.id.progress_bar_loading_movie_list)
    ProgressBar progressBar;

    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;

    public MoviesGridFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_movies_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = ViewModelProviders.of(this).get(MovieGridViewModel.class);
        viewModel.refresh();

        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(gridAdapter);

        observeViewModel();
    }

    private void observeViewModel(){
        viewModel.getMovies().observe(this, movies -> {
            if (movies != null){
                recyclerView.setVisibility(View.VISIBLE);
                gridAdapter.updateMovieList(movies);
            }
        });

        viewModel.getMovieLoadError().observe(this, isError -> {
            if (isError != null){
                errorTextView.setVisibility(isError ? View.VISIBLE : View.GONE);
            }
        });

        viewModel.getLoading().observe(this, isLoading -> {
            if (isLoading != null){
                progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
                if (isLoading){
                    errorTextView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                }
            }
        });
    }
}
