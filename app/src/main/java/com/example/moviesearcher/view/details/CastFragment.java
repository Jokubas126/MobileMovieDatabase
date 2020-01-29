package com.example.moviesearcher.view.details;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviesearcher.R;
import com.example.moviesearcher.viewmodel.CastViewModel;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CastFragment extends Fragment {

    @BindView(R.id.crew_layout) LinearLayout crewLayout;
    @BindView(R.id.cast_layout) LinearLayout castLayout;
    @BindView(R.id.progress_bar_loading_cast) ProgressBar progressBar;
    @BindView(R.id.cast_recycler_view) RecyclerView castView;
    @BindView(R.id.crew_recycler_view) RecyclerView crewView;

    private PeopleAdapter castAdapter;
    private PeopleAdapter crewAdapter;
    private CastViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cast, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = ViewModelProviders.of(this).get(CastViewModel.class);
        viewModel.fetch(getArguments());

        castAdapter = new PeopleAdapter(getActivity(), new ArrayList<>());
        castView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        castView.setAdapter(castAdapter);

        crewAdapter = new PeopleAdapter(getActivity(), new ArrayList<>());
        crewView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        crewView.setAdapter(crewAdapter);

        observeViewModel();
    }

    private synchronized void observeViewModel(){
        viewModel.getCast().observe(this, cast -> {
            if (cast != null) {
                castLayout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                castAdapter.updatePeopleList(cast);
            }
        });

        viewModel.getCrew().observe(this, crew -> {
            if (crew != null){
                crewLayout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                crewAdapter.updatePeopleList(crew);
            }
        });

        viewModel.getLoading().observe(this, isLoading -> {
            if(isLoading != null){
                progressBar.setVisibility(isLoading? View.VISIBLE : View.GONE);
                if (isLoading){
                    castLayout.setVisibility(View.GONE);
                    crewLayout.setVisibility(View.GONE);
                }
            }
        });
    }
}
