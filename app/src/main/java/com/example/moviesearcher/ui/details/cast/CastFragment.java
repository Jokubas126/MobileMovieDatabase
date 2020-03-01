package com.example.moviesearcher.ui.details.cast;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.moviesearcher.R;
import com.example.moviesearcher.util.BundleUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CastFragment extends Fragment implements BottomNavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.crew_layout)
    LinearLayout crewLayout;
    @BindView(R.id.cast_layout)
    LinearLayout castLayout;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.cast_recycler_view)
    RecyclerView castView;
    @BindView(R.id.crew_recycler_view)
    RecyclerView crewView;
    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;

    private PeopleAdapter castAdapter = new PeopleAdapter();
    private PeopleAdapter crewAdapter = new PeopleAdapter();
    private CastViewModel viewModel;

    public CastFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_cast, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(CastViewModel.class);
        viewModel.fetch(getActivity(), getArguments());

        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        castView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        castView.setItemAnimator(new DefaultItemAnimator());
        castView.setAdapter(castAdapter);

        crewView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        crewView.setItemAnimator(new DefaultItemAnimator());
        crewView.setAdapter(crewAdapter);

        observeViewModel();
    }

    private void observeViewModel() {
        viewModel.getCast().observe(getViewLifecycleOwner(), cast -> {
            if (cast != null) {
                castLayout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                castAdapter.updatePeopleList(cast);
            }
        });

        viewModel.getCrew().observe(getViewLifecycleOwner(), crew -> {
            if (crew != null) {
                crewLayout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                crewAdapter.updatePeopleList(crew);
            }
        });

        viewModel.getLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading != null) {
                progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
                if (isLoading) {
                    castLayout.setVisibility(View.GONE);
                    crewLayout.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.media_menu_item:
                if (getArguments() != null) {
                    NavDirections action = CastFragmentDirections.actionMovieMedia(getArguments().getInt(BundleUtil.KEY_MOVIE_ID));
                    Navigation.findNavController(bottomNavigationView).navigate(action);
                }
                break;

            case R.id.overview_menu_item:
                if (getArguments() != null) {
                    NavDirections action = CastFragmentDirections.actionMovieOverview(getArguments().getInt(BundleUtil.KEY_MOVIE_ID));
                    Navigation.findNavController(bottomNavigationView).navigate(action);
                }
                break;
        }

        return false;
    }
}
