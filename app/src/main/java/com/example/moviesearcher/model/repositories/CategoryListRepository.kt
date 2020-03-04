package com.example.moviesearcher.model.repositories

import com.example.moviesearcher.model.data.Category
import com.example.moviesearcher.model.data.Subcategory
import com.example.moviesearcher.model.services.MovieDbApiService
import com.example.moviesearcher.model.services.responses.GenresMapAsyncResponse
import com.example.moviesearcher.model.services.responses.SubcategoryListAsyncResponse
import java.util.*

class CategoryListRepository() {

    private val categoryList: MutableList<Category> = ArrayList()

    interface CategoryListResponse {
        fun onProcessFinished(categoryList: List<Category>?)
    }

    fun fetchData(callback: CategoryListResponse) {
        MovieDbApiService().getGenres(
                GenresMapAsyncResponse { genresMap ->
                    categoryList.add(genreMapToCategory(genresMap!!))

                    MovieDbApiService().getLanguages(SubcategoryListAsyncResponse {
                        sortList(it)
                        categoryList.add(Category("Language", it))
                        callback.onProcessFinished(categoryList)
                    })
                })
    }

    private fun genreMapToCategory(genresMap: HashMap<Int, String>): Category {
        val genreIds: List<Int> = ArrayList(genresMap.keys)
        val genreNames: List<String> = ArrayList(genresMap.values)
        val subcategoryList: MutableList<Subcategory> = ArrayList()
        for (i in genreIds.indices) {
            subcategoryList.add(Subcategory(genreIds[i], genreNames[i]))
        }
        sortList(subcategoryList)
        return Category("Genres", subcategoryList)
    }

    private fun sortList(list: List<Subcategory>) {
        Collections.sort(list) { o1: Subcategory, o2: Subcategory -> o1.name.compareTo(o2.name) }
    }
}