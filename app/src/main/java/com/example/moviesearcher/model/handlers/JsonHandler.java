package com.example.moviesearcher.model.handlers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.moviesearcher.model.Movie;
import com.example.moviesearcher.model.util.JsonUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JsonHandler {

    private List<Movie> movieList = new ArrayList<>();
    private int currentMovie = 0;

    private HashMap<Integer, String> genres = new HashMap<>();

    public List<Movie> getMoviesFromJson(final MovieListAsyncResponse callback){

        genres = getGenres(genresMap -> {
            genres.putAll(genresMap);
        });
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, JsonUtil.POPULAR_MOVIE_LIST_URL, null,
                response -> {
                    try{
                        JSONArray jsonArray = response.getJSONArray("results");
                        for (int i = 0; i < jsonArray.length(); i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Movie movie = new Movie();


                            Thread t = new Thread(() -> {
                                try{
                                    URL imageURL = new URL(JsonUtil.BASE_PICTURE_URL + jsonObject.getString("poster_path"));
                                    Bitmap image = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
                                    movie.setCoverImage(image);
                                } catch (IOException | JSONException e){ e.printStackTrace(); }

                            });
                            t.start();

                            movie.setTitle(jsonObject.getString("title"));
                            movie.setReleaseDate(jsonObject.getString("release_date"));
                            movie.setIMDbScore(jsonObject.getString("vote_average"));
                            JSONArray genresIds = jsonObject.getJSONArray("genre_ids");
                            List<String> genresList = new ArrayList<>();
                            for (int j = 0; j < genresIds.length(); j++){
                                genresList.add(genres.get(genresIds.getInt(j)));
                            }
                            movie.setGenres(genresList);
                            try {
                                t.join();
                            } catch (InterruptedException e) { e.printStackTrace(); }
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

    public HashMap<Integer, String> getGenres( final GenresMapAsyncResponse callback){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, JsonUtil.MOVIE_GENRES_URL, null,
                response -> {
                    try {
                        JSONArray array = response.getJSONArray("genres");
                    for (int i = 0; i < array.length(); i++){
                            int id = array.getJSONObject(i).getInt("id");
                            String name = array.getJSONObject(i).getString("name");
                            genres.put(
                                    array.getJSONObject(i).getInt("id"),
                                    array.getJSONObject(i).getString("name")
                            );
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("JSONArrayRequest", "getGenres: EXCEPTION OCCURRED");}
                    if (callback != null) callback.processFinished(genres);
                }, error -> Log.d("JSONArrayRequest", "getGenres: ERROR OCCURRED"));

        ApplicationRequestHandler.getInstance().addToRequestQueue(jsonObjectRequest);
        return genres;
    }

    public List<Movie> getMovieList() {
        return movieList;
    }
}
