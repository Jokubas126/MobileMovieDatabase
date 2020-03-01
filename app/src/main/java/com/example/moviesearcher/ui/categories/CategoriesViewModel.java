package com.example.moviesearcher.ui.categories;

import android.app.Activity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moviesearcher.model.data.Category;
import com.example.moviesearcher.model.repositories.CategoryListRepository;

import java.util.List;

public class CategoriesViewModel extends ViewModel {

    private MutableLiveData<List<Category>> categories = new MutableLiveData<>();
    private MutableLiveData<Boolean> loading = new MutableLiveData<>();

    public void fetch(Activity activity){
        loading.setValue(true);

        new CategoryListRepository().fetchData(categoryList -> {
            if (categoryList != null){
                activity.runOnUiThread(() -> {
                    categories.setValue(categoryList);
                    loading.setValue(false);
                });
            }
        });
    }

    public LiveData<List<Category>> getCategories() {
        return categories;
    }

    public LiveData<Boolean> getLoading() {
        return loading;
    }
}
