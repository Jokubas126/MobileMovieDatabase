package com.example.moviesearcher.model.repositories;

import com.example.moviesearcher.model.data.Category;
import com.example.moviesearcher.model.data.Subcategory;
import com.example.moviesearcher.model.handlers.JsonHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class CategoryListRepository {

    private List<Category> categoryList = new ArrayList<>();

    public interface CategoryListResponse{
        void onProcessFinished(List<Category> categoryList);
    }

    public void fetchData(CategoryListResponse callback){
        new JsonHandler().getGenres(genresMap -> {

            categoryList.add(genreMapToCategory(genresMap));

            callback.onProcessFinished(categoryList);
        });
    }

    private Category genreMapToCategory(HashMap<Integer, String> genresMap){
        List<Integer> genreIds = new ArrayList<>(genresMap.keySet());
        List<String> genreNames = new ArrayList<>(genresMap.values());

        List<Subcategory> subcategoryList = new ArrayList<>();
        for (int i = 0; i < genreIds.size(); i++){
            subcategoryList.add(new Subcategory(genreIds.get(i), genreNames.get(i)));
        }
        Collections.sort(subcategoryList, (o1, o2) -> o1.getName().compareTo(o2.getName()));
        return new Category("Genres", subcategoryList);
    }
}
