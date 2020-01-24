package com.example.moviesearcher.view;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.moviesearcher.R;
import com.example.moviesearcher.model.Movie;
import com.example.moviesearcher.model.MovieGridAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class MoviesListFragment extends Fragment {

    public MoviesListFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_movies_list, container, false);
        ButterKnife.bind(this, view);

        List<Movie> movies = new ArrayList<>();
        movies.add(new Movie("random"));
        movies.add(new Movie("random"));
        movies.add(new Movie("random"));
        movies.add(new Movie("random"));
        movies.add(new Movie("random"));
        movies.add(new Movie("random"));
        movies.add(new Movie("random"));
        movies.add(new Movie("random"));

        RecyclerView gridView = (RecyclerView) view.findViewById(R.id.movie_grid_view);


        MovieGridAdapter viewAdapter = new MovieGridAdapter(getActivity(), new ArrayList<>(movies));
        gridView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        gridView.setItemAnimator(new DefaultItemAnimator());
        gridView.setAdapter(viewAdapter);

        return view;
    }
}
