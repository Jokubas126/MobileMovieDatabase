package com.jokubas.mmdb.model.remote.repositories

import com.jokubas.mmdb.model.data.entities.*
import com.jokubas.mmdb.model.remote.services.MovieService
import com.jokubas.mmdb.util.constants.MOVIE_DB_API_KEY
import kotlinx.coroutines.flow.flow

class CategoryRepository(
    private val service: MovieService
) {

    fun getCategories() = flow {
        emit(listOf(getGenresCategory(), getLanguagesCategory()))
    }

    private suspend fun getLanguagesCategory(): Category =
        Category(
            type = CategoryType.LANGUAGES,
            subcategoryList = service.languages(MOVIE_DB_API_KEY).sortedBy { it.name }
        )

    private suspend fun getGenresCategory(): Category =
        Category(
            type = CategoryType.GENRES,
            subcategoryList = service.genres(MOVIE_DB_API_KEY).genreList.map {
                Subcategory(it.id.toString(), it.name)
            }.sortedBy { it.name }
        )
}