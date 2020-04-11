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

    fun insertOrUpdateGenre(genre: Genre) {
        launch { insertOrUpdateGenreBG(genre) }
    }

    private suspend fun insertOrUpdateGenreBG(genre: Genre) {
        withContext(Dispatchers.IO) {
            genreDao.insertOrUpdateGenre(genre)
        }
    }

    fun updateGenres(genreList: List<Genre>) {
        launch { updateGenresBG(genreList) }
    }

    private suspend fun updateGenresBG(genreList: List<Genre>) {
        withContext(Dispatchers.IO){
            genreDao.deleteAllGenres()
            for (genre in genreList)
                genreDao.insertOrUpdateGenre(genre)
        }
    }

    fun getGenreById(genreId: Int) = genreDao.getGenreById(genreId)

    fun deleteAllGenres() {
        launch { deleteAllGenresBG() }
    }

    private suspend fun deleteAllGenresBG() {
        withContext(Dispatchers.IO) {
            genreDao.deleteAllGenres()
        }
    }
}