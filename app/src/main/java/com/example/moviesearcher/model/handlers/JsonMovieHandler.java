package com.example.moviesearcher.model.handlers;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.moviesearcher.model.Movie;
import com.example.moviesearcher.model.util.JsonUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JsonMovieHandler {

    private List<Movie> movieList = new ArrayList<>();
    private int currentMovie = 0;

    public List<Movie> getMoviesFromJson(final MovieListAsyncResponse callback){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, JsonUtil.POPULAR_MOVIE_LIST_URL, null,
                response -> {
                    try{
                        JSONArray jsonArray = response.getJSONArray("results");
                        for (int i = 0; i < jsonArray.length(); i++){
                            String title = jsonArray.getJSONObject(i).getString("original_title");

                            Movie movie = new Movie();
                            movie.setTitle(title);
                            movieList.add(movie);
                        }

                    }catch (JSONException e){
                        e.printStackTrace();

                    }
                    if (callback != null) callback.processFinished((ArrayList<Movie>) movieList);
            }, error -> {
            Log.d("JSONArrayRequest", "getMoviesFromJson: ERROR OCCURRED");
            });
        ApplicationRequestHandler.getInstance().addToRequestQueue(jsonObjectRequest);
        return movieList;
    }

    public List<Movie> getMovieList() {
        return movieList;
    }
}
