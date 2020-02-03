package com.example.moviesearcher.viewmodel;

import android.app.Activity;
import android.os.Bundle;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moviesearcher.model.data.Video;
import com.example.moviesearcher.model.handlers.JsonHandler;
import com.example.moviesearcher.util.BundleUtil;

import java.util.List;

public class MovieMediaViewModel extends ViewModel {

    private MutableLiveData<Video> trailer = new MutableLiveData<>();
    private MutableLiveData<List<String>> posterList = new MutableLiveData<>();
    private MutableLiveData<List<String>> backdropList = new MutableLiveData<>();
    private MutableLiveData<Boolean> loading = new MutableLiveData<>();

    public void fetch(Activity activity, Bundle args){
        loading.setValue(true);
        if (args != null){
            new JsonHandler().getTrailer(args.getInt(BundleUtil.KEY_MOVIE_ID),
                    video -> activity.runOnUiThread(() -> {
                        trailer.setValue((Video) video);
                        loading.setValue(false);
                    }));
            new JsonHandler().getImages(args.getInt(BundleUtil.KEY_MOVIE_ID),
                    (backdropPathList, posterPathList) ->
                            activity.runOnUiThread(() -> {
                                posterList.setValue(posterPathList);
                                backdropList.setValue(backdropPathList);
                    }));
        }
    }

    public MutableLiveData<Video> getTrailer() {
        return trailer;
    }

    public MutableLiveData<List<String>> getPosterList() {
        return posterList;
    }

    public MutableLiveData<List<String>> getBackdropList() {
        return backdropList;
    }

    public MutableLiveData<Boolean> getLoading() {
        return loading;
    }
}
