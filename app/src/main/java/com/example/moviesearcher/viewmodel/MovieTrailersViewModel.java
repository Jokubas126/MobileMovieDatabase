package com.example.moviesearcher.viewmodel;

import android.os.Bundle;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MovieTrailersViewModel extends ViewModel {

    private MutableLiveData<Boolean> loading = new MutableLiveData<>();

    public void fetch(Bundle args){
        loading.setValue(true);
        if (args != null){
            // here data has to be fetched
            loading.setValue(false);
        }
    }

    public MutableLiveData<Boolean> getLoading() {
        return loading;
    }
}
