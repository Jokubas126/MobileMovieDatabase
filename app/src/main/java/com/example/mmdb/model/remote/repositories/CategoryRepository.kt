package com.example.mmdb.model.remote.repositories

import android.app.Application
import com.example.mmdb.model.data.Category
import com.example.mmdb.model.data.Genres
import com.example.mmdb.model.data.Subcategory
import com.example.mmdb.model.remote.services.MovieApiService
import com.example.mmdb.util.*
import kotlinx.coroutines.flow.flow
import java.util.*
import java.util.Collections.sort

class CategoryRepository(private val application: Application){

    private var service = MovieApiService.api

    fun getCategories() = flow {
        if (isNetworkAvailable(application))
            emit(listOf(getGenresCategory(), getLanguagesCategory()))
        else {
            networkUnavailableNotification(application)
            emit(null)
        }
    }

    private suspend fun getLanguagesCategory(): Category? {
        val languages = service.getLanguages(MOVIE_DB_API_KEY)
        return Category(LANGUAGE_CATEGORY, formatSubcategories(languages))
    }

    private suspend fun getGenresCategory(): Category? {
        val genres = service.getGenres(MOVIE_DB_API_KEY)
        val subcategoryList = genresToSubcategoryList(genres)
        return Category(GENRE_CATEGORY, formatSubcategories(subcategoryList))
    }

    private fun genresToSubcategoryList(genres: Genres): List<Subcategory> {
        val subcategoryList = mutableListOf<Subcategory>()
        for (genre in genres.genreList) {
            subcategoryList.add(Subcategory(genre.id.toString(), genre.name))
        }
        return subcategoryList
    }

    private fun formatSubcategories(list: List<Subcategory>): List<Subcategory> {
        sort(list) { o1: Subcategory, o2: Subcategory -> o1.name.compareTo(o2.name) }
        // to have an empty item at the beginning for deselection
        (list as MutableList<Subcategory>).add(0, Subcategory("", ""))
        return list
    }
}