package com.example.moviesearcher.model.handlers;

import android.graphics.BitmapFactory;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.moviesearcher.model.data.Movie;
import com.example.moviesearcher.model.data.Person;
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

    private HashMap<Integer, String> genres = new HashMap<>();

    public void getMovieList(final MovieListAsyncResponse callback){
        new Thread(() -> {
        List<Movie> movieList = new ArrayList<>();
        genres = getGenres(genresMap -> genres.putAll(genresMap));
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, JsonUtil.POPULAR_MOVIE_LIST_URL, null,
                response -> {
                    try{
                        JSONArray jsonArray = response.getJSONArray("results");
                        for (int i = 0; i < jsonArray.length(); i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Movie movie = new Movie();

                            Thread thread = new Thread(() -> {
                                try{
                                    movie.setPosterImage(
                                            BitmapFactory.decodeStream(
                                                    new URL(JsonUtil.getInstance().getPosterImageUrl(jsonObject.getString("poster_path")))
                                                            .openStream()));
                                } catch (IOException | JSONException e){
                                    e.printStackTrace();
                                    Log.d("JSONArrayRequest", "getMovieList: EXCEPTION GETTING POSTER");
                                }
                            });
                            thread.start();

                            movie.setId(jsonObject.getInt("id"));
                            movie.setTitle(jsonObject.getString("title"));
                            movie.setReleaseDate(jsonObject.getString("release_date"));
                            movie.setScore(jsonObject.getString("vote_average"));

                            JSONArray genresIds = jsonObject.getJSONArray("genre_ids");
                            List<String> genresList = new ArrayList<>();
                            for (int j = 0; j < genresIds.length(); j++){
                                genresList.add(genres.get(genresIds.getInt(j)));
                            }
                            movie.setGenres(genresList);

                            try {
                                thread.join();
                            } catch (InterruptedException e) { Log.d("JSONArrayRequest", "getMovieList: EXCEPTION JOINING THREADS"); }
                            movieList.add(movie);
                        }

                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                    if (callback != null) callback.processFinished(movieList);
            }, error -> Log.d("JSONArrayRequest", "getMovieList: ERROR OCCURRED"));
        ApplicationRequestHandler.getInstance().addToRequestQueue(request);
        }).start();
    }

    private HashMap<Integer, String> getGenres( final GenresMapAsyncResponse callback){
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, JsonUtil.MOVIE_GENRES_URL, null,
                response -> {
                    try {
                        JSONArray array = response.getJSONArray("genres");
                    for (int i = 0; i < array.length(); i++){
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

        ApplicationRequestHandler.getInstance().addToRequestQueue(request);
        return genres;
    }

    public void getMovieDetails(int movieId, final ObjectAsyncResponse callback){
        new Thread(() -> {
        Movie movie = new Movie();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                JsonUtil.getInstance().getMovieDetailsUrl(movieId), null,
                response -> {
                    try {
                        Thread threadPoster = new Thread(() -> {
                            try {
                                movie.setPosterImage(
                                        BitmapFactory.decodeStream(
                                                new URL(JsonUtil.getInstance().getPosterImageUrl(response.getString("poster_path")))
                                                        .openStream()));
                            } catch (IOException | JSONException e) {
                                Log.d("JSONArrayRequest", "getMovieDetails: EXCEPTION GETTING POSTER");
                                e.printStackTrace();
                            }
                        });
                        threadPoster.start();

                        Thread threadBackdrop = new Thread(() -> {
                            try {
                                movie.setBackdropImage(
                                        BitmapFactory.decodeStream(
                                                new URL(JsonUtil.getInstance().getBackdropImageUrl(response.getString("backdrop_path")))
                                                        .openStream()));
                            } catch (IOException | JSONException e) {
                                Log.d("JSONArrayRequest", "getMovieDetails: EXCEPTION GETTING POSTER");
                                e.printStackTrace();
                            }
                        });
                        threadBackdrop.start();

                        movie.setId(movieId);
                        movie.setTitle(response.getString("title"));
                        movie.setReleaseDate(response.getString("release_date"));

                        JSONArray countries = response.getJSONArray("production_countries");
                        List<String> countryList = new ArrayList<>();
                        for (int i = 0; i < countries.length(); i++){
                            countryList.add(countries.getJSONObject(i).getString("iso_3166_1"));
                        }
                        movie.setProductionCountries(countryList);

                        JSONArray genres = response.getJSONArray("genres");
                        List<String> genresList = new ArrayList<>();
                        for (int i = 0; i < genres.length(); i++){
                            genresList.add(genres.getJSONObject(i).getString("name"));
                        }
                        movie.setGenres(genresList);

                        movie.setRuntime(response.getInt("runtime"));
                        movie.setScore(response.getString("vote_average"));
                        movie.setDescription(response.getString("overview"));

                        try {
                            threadPoster.join();
                            threadBackdrop.join();
                        } catch (InterruptedException e) { Log.d("JSONArrayRequest", "getMovieDetails: EXCEPTION JOINING THREADS"); }
                    } catch (JSONException e){
                        e.printStackTrace();
                        Log.d("JSONArrayRequest", "getMovieDetails: EXCEPTION OCCURRED");
                    }
                    if (callback != null) callback.processFinished(movie);
                }, error -> {
                    Log.d("JSONArrayRequest", "getMovieDetails: ERROR OCCURRED");
                });
        ApplicationRequestHandler.getInstance().addToRequestQueue(request);
        }).start();
    }

    public void getPeople(int movieId, PersonListAsyncResponse callback) {
        Thread methodThread = new Thread(() -> {
        List<Person> cast = new ArrayList<>();
        List<Person> crew = new ArrayList<>();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, JsonUtil.getInstance().getPeopleUrl(movieId), null,
                response -> {
                    try {
                        JSONArray castArray = response.getJSONArray("cast");
                        for (int i = 0; i < castArray.length(); i++){
                            if (i > 9)
                                break;
                            Person person = new Person();
                            JSONObject object = castArray.getJSONObject(i);

                            Thread thread = new Thread(() -> {
                                try {
                                    person.setProfileImage(BitmapFactory.decodeStream(
                                            new URL(JsonUtil.getInstance().getProfileImageUrl(
                                                    object.getString("profile_path"))).openStream()));
                                } catch (JSONException | IOException e) { e.printStackTrace(); }
                            });
                            thread.start();

                            person.setName(object.getString("name"));
                            person.setPosition(object.getString("character"));

                            thread.join();
                            cast.add(person);
                        }
                        JSONArray crewArray = response.getJSONArray("crew");
                        for (int i = 0; i < crewArray.length(); i++){
                            if (i > 9)
                                break;
                            Person person = new Person();
                            JSONObject object = crewArray.getJSONObject(i);

                            Thread thread = new Thread(() -> {
                                try {
                                    person.setProfileImage(BitmapFactory.decodeStream(
                                            new URL(JsonUtil.getInstance().getProfileImageUrl(
                                                    object.getString("profile_path"))).openStream()));
                                } catch (JSONException | IOException e) { e.printStackTrace(); }
                            });
                            thread.start();

                            person.setName(object.getString("name"));
                            person.setPosition(object.getString("job"));

                            thread.join();
                            crew.add(person);
                        }
                    } catch (JSONException e) { e.printStackTrace(); }
                    if (callback != null) callback.processFinished(cast, crew);
                }, error -> {});
        ApplicationRequestHandler.getInstance().addToRequestQueue(request);
        });
        methodThread.start();
        try {
            methodThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
