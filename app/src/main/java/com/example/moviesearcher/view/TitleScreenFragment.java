package com.example.moviesearcher.view;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.moviesearcher.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TitleScreenFragment extends Fragment {

    @BindView(R.id.title_screen_proceed_button) ImageButton proceedButton;

    public TitleScreenFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_title_screen, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        proceedButton.setOnClickListener(v -> onGoToMoviesList());
    }


    private void onGoToMoviesList(){
        NavDirections action = TitleScreenFragmentDirections.actionMoviesList();
        Navigation.findNavController(proceedButton).navigate(action);
    }
}
