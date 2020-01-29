package com.example.moviesearcher.viewmodel;

import android.os.Bundle;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DescriptionViewModel extends ViewModel {

    private MutableLiveData<String> description = new MutableLiveData<>();

    public void fetch(Bundle args){
        if (args != null){
            description.setValue(args.getString("description"));
        }
    }

    public MutableLiveData<String> getDescription() {
        return description;
    }
}
