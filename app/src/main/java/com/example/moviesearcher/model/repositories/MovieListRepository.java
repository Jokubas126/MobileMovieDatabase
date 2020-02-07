package com.example.moviesearcher.model.repositories;

import android.util.Log;

import com.example.moviesearcher.model.data.Movie;
import com.example.moviesearcher.model.handlers.responses.MovieListAsyncResponse;
import com.example.moviesearcher.util.MovieDbUtil;
import com.example.moviesearcher.util.UrlUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MovieListRepository {

    public void fetchData(JSONObject objectResponse, HashMap<Integer, String> genres, MovieListAsyncResponse callback){
        Thread movieListThread = new Thread(() -> {
            List<Movie> movieList = new ArrayList<>();
            try {
                JSONArray jsonArray = objectResponse.getJSONArray(MovieDbUtil.KEY_RESULT_ARRAY);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Movie movie = new Movie();

                    movie.setPosterImageUrl(UrlUtil.getImageUrl(jsonObject.getString(MovieDbUtil.KEY_POSTER_PATH)));
                    movie.setId(jsonObject.getInt(MovieDbUtil.KEY_ID));
                    movie.setTitle(jsonObject.getString(MovieDbUtil.KEY_MOVIE_TITLE));
                    movie.setReleaseDate(jsonObject.getString(MovieDbUtil.KEY_RELEASE_DATE));
                    movie.setScore(jsonObject.getString(MovieDbUtil.KEY_MOVIE_SCORE));

                    JSONArray genresIds = jsonObject.getJSONArray(MovieDbUtil.KEY_MOVIE_GENRE_IDS_ARRAY);
                    List<String> genresList = new ArrayList<>();
                    for (int j = 0; j < genresIds.length(); j++) {
                        genresList.add(genres.get(genresIds.getInt(j)));
                    }
                    movie.setGenres(genresList);
                    movieList.add(movie);
                }
            } catch (JSONException e) {
                Log.d("JSONArrayRequest", "getMovieList: EXCEPTION OCCURRED");
            }
            if (callback != null) callback.processFinished(movieList);
        });
        movieListThread.setPriority(6);
        movieListThread.start();
    }
}
