package com.example.moviesearcher.model.repositories

import android.util.Log
import com.example.moviesearcher.model.data.Movie
import com.example.moviesearcher.model.services.responses.MovieListAsyncResponse
import com.example.moviesearcher.util.MovieDbUtil
import com.example.moviesearcher.util.UrlUtil
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class MovieListRepository() {

    fun fetchData(objectResponse: JSONObject, genres: HashMap<Int?, String?>, callback: MovieListAsyncResponse?) {
        val movieListThread = Thread(Runnable {
            val movieList: MutableList<Movie> = ArrayList()
            try {
                val jsonArray = objectResponse.getJSONArray(MovieDbUtil.KEY_RESULT_ARRAY)
                for (i in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(i)
                    val movie = Movie()
                    movie.posterImageUrl = UrlUtil.getImageUrl(jsonObject.getString(MovieDbUtil.KEY_POSTER_PATH))
                    movie.id = jsonObject.getInt(MovieDbUtil.KEY_ID)
                    movie.title = jsonObject.getString(MovieDbUtil.KEY_MOVIE_TITLE)
                    movie.releaseDate = jsonObject.getString(MovieDbUtil.KEY_RELEASE_DATE)
                    movie.score = jsonObject.getString(MovieDbUtil.KEY_MOVIE_SCORE)
                    val genresIds = jsonObject.getJSONArray(MovieDbUtil.KEY_MOVIE_GENRE_IDS_ARRAY)
                    val genresList: MutableList<String?> = ArrayList()
                    for (j in 0 until genresIds.length()) {
                        genresList.add(genres[genresIds.getInt(j)])
                    }
                    movie.setGenres(genresList)
                    movieList.add(movie)
                }
            } catch (e: JSONException) {
                Log.d("JSONArrayRequest", "getMovieList: EXCEPTION OCCURRED")
            }
            callback?.processFinished(movieList)
        })
        movieListThread.priority = 6
        movieListThread.start()
    }
}