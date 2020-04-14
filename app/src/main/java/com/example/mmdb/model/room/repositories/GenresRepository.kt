package com.example.mmdb.model.room.repositories

import android.content.Context
import com.example.mmdb.model.data.Genre
import com.example.mmdb.model.room.databases.GenresDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

class GenresRepository(context: Context) : CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private val genreDao = GenresDatabase.getInstance(context).genresDao()

    fun updateGenresBG(genreList: List<Genre>) {
        genreDao.updateGenres(genreList)
    }

    fun getAnyGenre() = genreDao.getAnyGenre()

    fun getGenreById(genreId: Int) = genreDao.getGenreById(genreId)

    fun getGenresByIdList(genreIdList: List<Int>) = genreDao.getGenresByIdList(genreIdList)
}