package com.example.moviesearcher.view.details;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.moviesearcher.R;
import com.example.moviesearcher.viewmodel.DescriptionViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DescriptionFragment extends Fragment {

    @BindView(R.id.movie_description_view) TextView descriptionView;

    private DescriptionViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_description, container,false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = ViewModelProviders.of(this).get(DescriptionViewModel.class);
        viewModel.fetch(getArguments());
        observeViewModel();
    }

    private void observeViewModel(){
        viewModel.getDescription().observe(this, description -> {
            if (description != null)
                descriptionView.setText(description);
        });
    }
}
