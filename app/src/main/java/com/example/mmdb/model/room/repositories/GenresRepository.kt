package com.example.mmdb.model.room.repositories

import android.content.Context
import com.example.mmdb.model.data.Genre
import com.example.mmdb.model.room.databases.GenresDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlin.coroutines.CoroutineContext

class GenresRepository(context: Context) : CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private val genreDao = GenresDatabase.getInstance(context).genresDao()

    fun updateGenres(genreList: List<Genre>) {
        genreDao.updateGenres(genreList)
    }

    suspend fun getAnyGenre() = genreDao.getAnyGenre()

    fun getGenresByIdList(genreIdList: List<Int>) = genreDao.getGenresByIdList(genreIdList)
}