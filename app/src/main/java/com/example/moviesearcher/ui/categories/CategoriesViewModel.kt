package com.example.moviesearcher.ui.categories

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import com.example.moviesearcher.model.data.Category
import com.example.moviesearcher.model.data.Genres
import com.example.moviesearcher.model.data.Subcategory
import com.example.moviesearcher.model.repositories.CategoryRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Collections.sort

class CategoriesViewModel : ViewModel() {

    private val _categories = MutableLiveData<MutableList<Category>>()
    private val _loading = MutableLiveData<Boolean>()

    val categories: LiveData<MutableList<Category>> = _categories
    val loading: LiveData<Boolean> = _loading

    fun fetch() {
        _loading.value = true
        getLanguages()
        getGenres()
    }

    private fun getLanguages() {
        CoroutineScope(Dispatchers.IO).launch {
            val response = CategoryRepository().getLanguages()
            withContext(Dispatchers.Main) {
                val subcategories = sortList(response.body()!!)
                var list = mutableListOf<Category>()
                if (!_categories.value.isNullOrEmpty())
                    list = categories.value!!
                list.add(Category("Languages", subcategories))
                _categories.value = list
                _loading.value = false
            }
        }
    }

    private fun getGenres() {
        CoroutineScope(Dispatchers.IO).launch {
            val response = CategoryRepository().getGenres()
            withContext(Dispatchers.Main) {
                val subcategories = sortList(genresToSubcategoryList(response.body()!!))
                var list = mutableListOf<Category>()
                if (!_categories.value.isNullOrEmpty())
                    list = categories.value!!
                list.add(Category("Genres", subcategories))
                _categories.value = list
                _loading.value = false
            }
        }
    }

    private fun genresToSubcategoryList(genres: Genres): List<Subcategory> {
        val subcategoryList: MutableList<Subcategory> = ArrayList()
        for (genre in genres.genreList) {
            subcategoryList.add(Subcategory(genre.id.toString(), genre.name))
        }
        return subcategoryList
    }

    private fun sortList(list: List<Subcategory>): List<Subcategory> {
        sort(list) { o1: Subcategory, o2: Subcategory -> o1.name.compareTo(o2.name) }
        return list
    }

    fun onSubcategoryClicked(view: View?, subcategory: Subcategory?, startYear: String, endYear: String) {
        val action: NavDirections = CategoriesFragmentDirections.actionMoviesList(subcategory, startYear, endYear, null)
        Navigation.findNavController(view!!).navigate(action)
    }
}