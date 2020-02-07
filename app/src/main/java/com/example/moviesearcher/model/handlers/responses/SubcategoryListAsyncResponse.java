package com.example.moviesearcher.model.handlers.responses;

import com.example.moviesearcher.model.data.Subcategory;

import java.util.List;

public interface SubcategoryListAsyncResponse {
    void processFinished(List<Subcategory> subcategoryList);
}
