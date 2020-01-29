package com.example.moviesearcher.viewmodel;

import android.os.Bundle;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moviesearcher.model.data.Person;
import com.example.moviesearcher.model.handlers.JsonHandler;

import java.util.List;

public class CastViewModel extends ViewModel {

    private MutableLiveData<List<Person>> cast = new MutableLiveData<>();
    private MutableLiveData<List<Person>> crew = new MutableLiveData<>();
    private MutableLiveData<Boolean> loading = new MutableLiveData<>();

    public void fetch(Bundle args){
        loading.setValue(true);
        new Thread(() -> {
            if (args != null){
                new JsonHandler().getPeople(args.getInt("movieId"),
                        (castList, crewList) -> {
                            cast.setValue(castList);
                            crew.setValue(crewList);
                            loading.setValue(false);
                        });
            }
        }).start();

    }

    public MutableLiveData<List<Person>> getCast() {
        return cast;
    }

    public MutableLiveData<List<Person>> getCrew() {
        return crew;
    }

    public MutableLiveData<Boolean> getLoading() {
        return loading;
    }
}
