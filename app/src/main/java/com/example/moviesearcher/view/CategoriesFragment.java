package com.example.moviesearcher.view;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.moviesearcher.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoriesFragment extends Fragment {


    public CategoriesFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categories, container, false);
        ButterKnife.bind( this, view);
        return view;
    }

}
