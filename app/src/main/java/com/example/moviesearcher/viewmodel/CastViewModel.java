package com.example.moviesearcher.viewmodel;

import android.app.Activity;
import android.os.Bundle;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moviesearcher.model.data.Person;
import com.example.moviesearcher.model.handlers.JsonHandler;
import com.example.moviesearcher.util.BundleUtil;

import java.util.List;

public class CastViewModel extends ViewModel {

    private MutableLiveData<List<Person>> cast = new MutableLiveData<>();
    private MutableLiveData<List<Person>> crew = new MutableLiveData<>();
    private MutableLiveData<Boolean> loading = new MutableLiveData<>();

    public void fetch(Activity activity, Bundle args){
        loading.setValue(true);
        new Thread(() -> {
            if (args != null){
                new JsonHandler().getPeople(args.getInt(BundleUtil.KEY_MOVIE_ID),
                        (castList, crewList) -> activity.runOnUiThread(() -> {
                            cast.setValue(castList);
                            crew.setValue(crewList);
                            loading.setValue(false);
                        }));
            }
        }).start();

    }

    public LiveData<List<Person>> getCast() {
        return cast;
    }

    public LiveData<List<Person>> getCrew() {
        return crew;
    }

    public LiveData<Boolean> getLoading() {
        return loading;
    }
}
