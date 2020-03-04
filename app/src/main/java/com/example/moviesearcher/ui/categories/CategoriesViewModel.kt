package com.example.moviesearcher.ui.categories

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moviesearcher.model.data.Category
import com.example.moviesearcher.model.repositories.CategoryListRepository

class CategoriesViewModel : ViewModel() {

    private val _categories = MutableLiveData<List<Category>>()
    private val _loading = MutableLiveData<Boolean>()

    val categories: LiveData<List<Category>> = _categories
    val loading: LiveData<Boolean> = _loading

    fun fetch(activity: Activity) {
        _loading.value = true
        CategoryListRepository().fetchData(object : CategoryListRepository.CategoryListResponse {
            override fun onProcessFinished(categoryList: List<Category>?) {
                if (categoryList != null) {
                    activity.runOnUiThread {
                        _categories.value = categoryList
                        _loading.value = false
                    }
                }
            }
        })
    }
}