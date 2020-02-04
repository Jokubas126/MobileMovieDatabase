package com.example.moviesearcher.view.categories;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.moviesearcher.R;
import com.example.moviesearcher.viewmodel.CategoriesViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoriesFragment extends Fragment {

    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.categories_recycler_view) RecyclerView recyclerView;

    private CategoryAdapter adapter;

    private CategoriesViewModel viewModel;

    public CategoriesFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categories, container, false);
        ButterKnife.bind( this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = ViewModelProviders.of(this).get(CategoriesViewModel.class);
        viewModel.fetch(getActivity());


        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        if (getContext() != null) // added this due to IDE complaining
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        observeViewModel();
    }

    private void observeViewModel(){
        viewModel.getCategories().observe(getViewLifecycleOwner(), categories -> {
            if (categories != null){
                if (recyclerView.getAdapter() == null){
                    adapter = new CategoryAdapter(categories);
                    recyclerView.setAdapter(adapter);
                } else {
                    CategoryAdapter adapter = new CategoryAdapter(categories);
                    recyclerView.swapAdapter(adapter, true);
                }
            }
        });

        viewModel.getLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading != null){
                progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
                recyclerView.setVisibility(isLoading ? View.GONE : View.VISIBLE);
            }
        });
    }
}
