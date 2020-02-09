package com.example.moviesearcher.view.categories;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.moviesearcher.R;
import com.example.moviesearcher.model.data.Subcategory;
import com.example.moviesearcher.viewmodel.CategoriesViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.apptik.widget.MultiSlider;

public class CategoriesFragment extends Fragment implements MultiSlider.OnThumbValueChangeListener, CategoryAdapter.OnSubcategoryClickedListener {

    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.categories_recycler_view) RecyclerView recyclerView;
    @BindView(R.id.release_year_slider) MultiSlider slider;
    @BindView(R.id.release_year_slider_min_value) TextView minYearView;
    @BindView(R.id.release_year_slider_max_value) TextView maxYearView;

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

        slider.setOnThumbValueChangeListener(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        if (getContext() != null) // added this due to IDE complaining
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        observeViewModel();
    }

    private void observeViewModel(){
        viewModel.getCategories().observe(getViewLifecycleOwner(), categories -> {
            if (categories != null){
                if (recyclerView.getAdapter() == null){
                    adapter = new CategoryAdapter(categories, this);
                    recyclerView.setAdapter(adapter);
                } else {
                    CategoryAdapter adapter = new CategoryAdapter(categories, this);
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

    @Override
    public void onValueChanged(MultiSlider multiSlider, MultiSlider.Thumb thumb, int thumbIndex, int value) {
        if (thumbIndex == 0)
            if (value != multiSlider.getMin())
                minYearView.setText(String.valueOf(value));
            else minYearView.setText("∞");
        if (thumbIndex == 1){
            maxYearView.setText(String.valueOf(value));
        }
    }

    @Override
    public void onSubcategoryClicked(View view, Subcategory subcategory) {
        NavDirections action = CategoriesFragmentDirections.actionMoviesList(subcategory, minYearView.getText().toString(), maxYearView.getText().toString(), null);
        Navigation.findNavController(view).navigate(action);
    }
}
