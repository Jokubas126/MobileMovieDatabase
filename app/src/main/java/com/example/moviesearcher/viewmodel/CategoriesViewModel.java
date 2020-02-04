package com.example.moviesearcher.viewmodel;

import android.app.Activity;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moviesearcher.model.data.Category;
import com.example.moviesearcher.model.data.Subcategory;

import java.util.ArrayList;
import java.util.List;

public class CategoriesViewModel extends ViewModel {

    private MutableLiveData<List<Category>> categories = new MutableLiveData<>();
    private MutableLiveData<Boolean> loading = new MutableLiveData<>();

    public void fetch(Activity activity){
        loading.setValue(true);

        List<Category> categoryList = new ArrayList<>();
        List<Subcategory> subcategoryList = new ArrayList<>();
        Subcategory subcategory1 = new Subcategory(0, "Action");
        Subcategory subcategory2 = new Subcategory(1, "Adventure");
        Subcategory subcategory3 = new Subcategory(2, "Comedy");
        Subcategory subcategory4 = new Subcategory(3, "Science Fiction");
        Subcategory subcategory5 = new Subcategory(4, "Sports");

        subcategoryList.add(subcategory1);
        subcategoryList.add(subcategory2);
        subcategoryList.add(subcategory3);
        subcategoryList.add(subcategory4);
        subcategoryList.add(subcategory5);

        Category category1 = new Category("Genres", subcategoryList);
        Category category2 = new Category("Another one", subcategoryList);

        categoryList.add(category1);
        categoryList.add(category2);
        categories.setValue(categoryList);
        loading.setValue(false);
    }

    public MutableLiveData<List<Category>> getCategories() {
        return categories;
    }

    public MutableLiveData<Boolean> getLoading() {
        return loading;
    }
}
