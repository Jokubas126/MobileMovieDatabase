package com.example.moviesearcher.model.room.repositories

import android.content.Context
import com.example.moviesearcher.model.data.Genre
import com.example.moviesearcher.model.room.databases.GenresDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class GenresRepository(context: Context) : CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private val genreDao = GenresDatabase.getInstance(context).genresDao()

    fun updateGenresBG(genreList: List<Genre>) {
        genreDao.updateGenres(genreList)
    }

    fun getGenreById(genreId: Int) = genreDao.getGenreById(genreId)

    fun getGenresByIdList(genreIdList: List<Int>) = genreDao.getGenresByIdList(genreIdList)
}