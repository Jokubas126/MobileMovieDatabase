package com.jokubas.mmdb.moviediscover.model.repositories

import com.jokubas.mmdb.moviediscover.model.entities.Category
import com.jokubas.mmdb.moviediscover.model.entities.CategoryType
import com.jokubas.mmdb.moviediscover.model.entities.Subcategory
import com.jokubas.mmdb.moviediscover.model.services.MovieDiscoverService
import com.jokubas.mmdb.util.DataResponse
import com.jokubas.mmdb.util.constants.MOVIE_DB_API_KEY
import com.jokubas.mmdb.util.defaultRemap
import com.jokubas.mmdb.util.toDataResponse
import kotlinx.coroutines.flow.flow

class CategoryRepository(
    private val service: MovieDiscoverService
) {

    fun categories() = flow<DataResponse<List<Category>>> {
        emit(DataResponse.Loading())

        val languages: DataResponse<Category> = languagesCategory()
        val genres: DataResponse<Category> = genresCategory()

        val responses = listOf(genres, languages)

        val categories: List<Category> =
            responses.filterIsInstance(DataResponse.Success::class.java)
                .mapNotNull { it.value as? Category }

        when {
            categories.isNotEmpty() -> {
                emit(DataResponse.Success(categories))
            }
            responses.any { it is DataResponse.Loading } -> emit(DataResponse.Loading())
            responses.any { it is DataResponse.Error } -> emit(DataResponse.Error())
            else -> emit(DataResponse.Empty())
        }
    }

    private suspend fun languagesCategory(): DataResponse<Category> =
        service.languages(MOVIE_DB_API_KEY).toDataResponse()
            .toCategoryDataResponse(CategoryType.LANGUAGES)

    private suspend fun genresCategory(): DataResponse<Category> =
        service.genres(MOVIE_DB_API_KEY).toDataResponse().defaultRemap { genres ->
            genres.genreList.map { Subcategory(it.id.toString(), it.name) }
        }.toCategoryDataResponse(CategoryType.GENRES)

    private fun DataResponse<List<Subcategory>>.toCategoryDataResponse(
        type: CategoryType
    ): DataResponse<Category> = defaultRemap { subcategoryList ->
        Category(
            type = type,
            subcategoryList = subcategoryList.sortedBy { it.name }
        )
    }
}