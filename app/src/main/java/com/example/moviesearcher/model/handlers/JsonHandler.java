package com.example.moviesearcher.model.handlers;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.moviesearcher.model.data.Movie;
import com.example.moviesearcher.model.data.Person;
import com.example.moviesearcher.model.data.Subcategory;
import com.example.moviesearcher.model.data.Video;
import com.example.moviesearcher.model.handlers.responses.GenresMapAsyncResponse;
import com.example.moviesearcher.model.handlers.responses.ImageListAsyncResponse;
import com.example.moviesearcher.model.handlers.responses.MovieListAsyncResponse;
import com.example.moviesearcher.model.handlers.responses.ObjectAsyncResponse;
import com.example.moviesearcher.model.handlers.responses.PersonListAsyncResponse;
import com.example.moviesearcher.model.handlers.responses.SubcategoryListAsyncResponse;
import com.example.moviesearcher.model.repositories.MovieListRepository;
import com.example.moviesearcher.util.MovieDbUtil;
import com.example.moviesearcher.util.UrlUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JsonHandler {

    private HashMap<Integer, String> genres = new HashMap<>();

    private int currentListTotalPages;

    public void getMovieList(String listKey, Subcategory subcategory, int page, final MovieListAsyncResponse callback){
        new Thread(() ->{
            genres = getGenres(genresMap -> genres.putAll(genresMap));
            String url = "";
            if (listKey != null)
                url = UrlUtil.getMovieListUrl(listKey, page);
            else if (subcategory != null)
                url = UrlUtil.getDiscoverUrl(subcategory.getId(), subcategory.getStringId(), page);
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                    response -> {
                        try {
                            currentListTotalPages = response.getInt(MovieDbUtil.KEY_TOTAL_PAGES);
                            if (page <= currentListTotalPages){
                                new MovieListRepository().fetchData(response, genres, callback);
                            } else if (callback != null){
                                callback.processFinished(null);
                            }
                        } catch (JSONException e) { e.printStackTrace(); }
                    },
                    error -> Log.d("JSONArrayRequest", "getMovieList: ERROR OCCURRED"));
            ApplicationRequestHandler.getInstance().addToRequestQueue(request);
        }).start();
    }

    public HashMap<Integer, String> getGenres( final GenresMapAsyncResponse callback){
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, UrlUtil.getMovieGenresUrl(), null,
                response -> {
                    Thread genreThread = new Thread (() -> {
                        try {
                            JSONArray array = response.getJSONArray(MovieDbUtil.KEY_MOVIE_GENRES_ARRAY);
                            for (int i = 0; i < array.length(); i++){
                                genres.put(
                                        array.getJSONObject(i).getInt(MovieDbUtil.KEY_ID),
                                        array.getJSONObject(i).getString(MovieDbUtil.KEY_NAME)
                                );
                            }
                        } catch (JSONException e) { Log.d("JSONArrayRequest", "getGenresList: EXCEPTION OCCURRED");}
                        if (callback != null) callback.processFinished(genres);
                    });
                    genreThread.setPriority(10);
                    genreThread.start();
                    try { genreThread.join();
                    } catch (InterruptedException e) { e.printStackTrace(); }
                }, error -> Log.d("JSONArrayRequest", "getGenresList: ERROR OCCURRED"));
        ApplicationRequestHandler.getInstance().addToRequestQueue(request);
        return genres;
    }

    public void getMovieDetails(int movieId, final ObjectAsyncResponse callback){
        new Thread(() -> {
        Movie movie = new Movie();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                UrlUtil.getMovieDetailsUrl(movieId), null,
                response -> new Thread(() -> {
                    try{
                        movie.setPosterImageUrl(UrlUtil.getImageUrl(response.getString(MovieDbUtil.KEY_POSTER_PATH)));
                        movie.setBackdropImageUrl(UrlUtil.getImageUrl(response.getString(MovieDbUtil.KEY_BACKDROP_PATH)));

                        movie.setId(movieId);
                        movie.setTitle(response.getString(MovieDbUtil.KEY_MOVIE_TITLE));
                        movie.setReleaseDate(response.getString(MovieDbUtil.KEY_RELEASE_DATE));

                        JSONArray countries = response.getJSONArray(MovieDbUtil.KEY_PRODUCTION_COUNTRIES_ARRAY);
                        List<String> countryList = new ArrayList<>();
                        for (int i = 0; i < countries.length(); i++){
                            countryList.add(countries.getJSONObject(i).getString(MovieDbUtil.KEY_COUNTRY_ISO_CODE));
                        }
                        movie.setProductionCountries(countryList);

                        JSONArray genres = response.getJSONArray(MovieDbUtil.KEY_MOVIE_GENRES_ARRAY);
                        List<String> genresList = new ArrayList<>();
                        for (int i = 0; i < genres.length(); i++){
                            genresList.add(genres.getJSONObject(i).getString(MovieDbUtil.KEY_NAME));
                        }
                        movie.setGenres(genresList);

                        movie.setRuntime(response.getInt(MovieDbUtil.KEY_MOVIE_RUNTIME));
                        movie.setScore(response.getString(MovieDbUtil.KEY_MOVIE_SCORE));
                        movie.setDescription(response.getString(MovieDbUtil.KEY_MOVIE_DESCRIPTION));

                    } catch (JSONException e){ Log.d("JSONArrayRequest", "getMovieDetails: EXCEPTION OCCURRED"); }
                    if (callback != null) callback.processFinished(movie);
                }).start(), error -> Log.d("JSONArrayRequest", "getMovieDetails: ERROR OCCURRED"));
        ApplicationRequestHandler.getInstance().addToRequestQueue(request);
        }).start();
    }

    public void getTrailer(int movieId, ObjectAsyncResponse callback){
        new Thread(() -> {
            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.GET, UrlUtil.getMovieVideosUrl(movieId), null,
                    response -> new Thread(() -> {
                        try {
                            JSONArray array = response.getJSONArray(MovieDbUtil.KEY_RESULT_ARRAY);
                            for (int i = 0; i < array.length(); i++){
                                JSONObject object = array.getJSONObject(i);
                                if (object.getString(MovieDbUtil.KEY_VIDEO_SITE).equals(MovieDbUtil.KEY_YOUTUBE_SITE)
                                        && object.getString(MovieDbUtil.KEY_VIDEO_TYPE).equals(MovieDbUtil.KEY_TRAILER_TYPE)){
                                    Video video = new Video();
                                    video.setKey(object.getString(MovieDbUtil.KEY_VIDEO));
                                    video.setName(object.getString(MovieDbUtil.KEY_NAME));
                                    if (callback != null) callback.processFinished(video);
                                    break;
                                }
                            }
                        } catch (JSONException e) {
                            Log.d("JSONObjectRequest", "getVideos: EXCEPTION OCCURRED"); }
                    }).start(), error -> Log.d("JSONObjectRequest", "getVideos: ERROR OCCURRED"));
            ApplicationRequestHandler.getInstance().addToRequestQueue(request);
        }).start();
    }

    public void getImages(int movieId, ImageListAsyncResponse callback){
        new Thread(() -> {
            List<String> backdropPaths = new ArrayList<>();
            List<String> posterPaths = new ArrayList<>();
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, UrlUtil.getMovieImagesUrl(movieId), null,
                    response -> new Thread (() -> {
                        try {
                            JSONArray backdropArray = response.getJSONArray("backdrops");
                            for (int i = 0; i < backdropArray.length(); i++){
                                backdropPaths.add(UrlUtil.getImageUrl(backdropArray.getJSONObject(i).getString("file_path")));
                            }
                            JSONArray posterArray = response.getJSONArray("posters");
                            for (int i = 0; i < posterArray.length(); i++){
                                if (i > 7)
                                    break;
                                posterPaths.add(UrlUtil.getImageUrl(posterArray.getJSONObject(i).getString("file_path")));
                            }
                        } catch (JSONException e) { Log.d("JSONObjectRequest", "getImages: EXCEPTION OCCURRED"); }
                        if(callback != null) callback.processFinished(backdropPaths, posterPaths);
                    }).start(), error -> Log.d("JSONObjectRequest", "getImages: ERROR OCCURRED"));
            ApplicationRequestHandler.getInstance().addToRequestQueue(request);
        }).start();
    }

    public void getPeople(int movieId, PersonListAsyncResponse callback) {
        new Thread(() -> {
        List<Person> cast = new ArrayList<>();
        List<Person> crew = new ArrayList<>();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, UrlUtil.getPeopleUrl(movieId), null,
                response -> new Thread(() -> {
                    try {
                        JSONArray castArray = response.getJSONArray(MovieDbUtil.KEY_CAST_ARRAY);
                        for (int i = 0; i < castArray.length(); i++){

                            Person person = new Person();
                            JSONObject object = castArray.getJSONObject(i);

                            person.setName(object.getString(MovieDbUtil.KEY_NAME));
                            person.setPosition(object.getString(MovieDbUtil.KEY_CAST_POSITION));
                            if (!object.getString(MovieDbUtil.KEY_PROFILE_IMAGE_PATH).equals("null"))
                                person.setProfileImageUrl(UrlUtil.getImageUrl(object.getString(MovieDbUtil.KEY_PROFILE_IMAGE_PATH)));
                            cast.add(person);
                        }
                        JSONArray crewArray = response.getJSONArray(MovieDbUtil.KEY_CREW_ARRAY);
                        for (int i = 0; i < crewArray.length(); i++){

                            Person person = new Person();
                            JSONObject object = crewArray.getJSONObject(i);
                            person.setName(object.getString(MovieDbUtil.KEY_NAME));
                            person.setPosition(object.getString(MovieDbUtil.KEY_CREW_POSITION));
                            if (!object.getString(MovieDbUtil.KEY_PROFILE_IMAGE_PATH).equals("null"))
                                person.setProfileImageUrl(UrlUtil.getImageUrl(object.getString(MovieDbUtil.KEY_PROFILE_IMAGE_PATH)));
                            crew.add(person);
                        }
                    } catch (JSONException e) { Log.d("JSONArrayRequest", "getPeople: EXCEPTION OCCURRED"); }
                    if (callback != null) callback.processFinished(cast, crew);
                }).start()
                , error -> Log.d("JSONArrayRequest", "getPeople: ERROR OCCURRED"));
        ApplicationRequestHandler.getInstance().addToRequestQueue(request);

        }).start();
    }

    public void getLanguages(SubcategoryListAsyncResponse callback){
        new Thread(() -> {
            List<Subcategory> subcategoryList = new ArrayList<>();

            JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, UrlUtil.getLanguagesUrl(), null,
                    response -> {
                        new Thread(() -> {
                            try {
                                for (int i = 0; i < response.length(); i++){
                                    JSONObject object = response.getJSONObject(i);
                                    subcategoryList.add(new Subcategory(object.getString(MovieDbUtil.KEY_LANGUAGE_ISO_CODE), object.getString(MovieDbUtil.KEY_ENGLISH_NAME)));
                                }
                            } catch (JSONException e) { e.printStackTrace(); }
                            if (callback != null) callback.processFinished(subcategoryList);
                        }).start();
                    }, error -> {

                    });
            ApplicationRequestHandler.getInstance().addToRequestQueue(request);
        }).start();
    }
}
