package com.example.moviesearcher.model.services;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.moviesearcher.model.data.Person;
import com.example.moviesearcher.model.data.Subcategory;
import com.example.moviesearcher.model.services.responses.PersonListAsyncResponse;
import com.example.moviesearcher.model.services.responses.SubcategoryListAsyncResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.moviesearcher.util.MovieDbConfigKt.*;
import static com.example.moviesearcher.util.MovieDbUtilKt.*;

public class MovieDbApiService {

    public void getPeople(int movieId, final PersonListAsyncResponse callback) {
        new Thread(() -> {
            List<Person> cast = new ArrayList<>();
            List<Person> crew = new ArrayList<>();
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, getPeopleUrl(movieId), null,
                    response -> new Thread(() -> {
                        try {
                            JSONArray castArray = response.getJSONArray(KEY_CAST_ARRAY);
                            for (int i = 0; i < castArray.length(); i++) {

                                Person person = new Person();
                                JSONObject object = castArray.getJSONObject(i);

                                person.setName(object.getString(KEY_NAME));
                                person.setPosition(object.getString(KEY_CAST_POSITION));
                                if (!object.getString(KEY_PROFILE_IMAGE_PATH).equals("null"))
                                    person.setProfileImageUrl(getImageUrl(object.getString(KEY_PROFILE_IMAGE_PATH)));
                                cast.add(person);
                            }
                            JSONArray crewArray = response.getJSONArray(KEY_CREW_ARRAY);
                            for (int i = 0; i < crewArray.length(); i++) {

                                Person person = new Person();
                                JSONObject object = crewArray.getJSONObject(i);
                                person.setName(object.getString(KEY_NAME));
                                person.setPosition(object.getString(KEY_CREW_POSITION));
                                if (!object.getString(KEY_PROFILE_IMAGE_PATH).equals("null"))
                                    person.setProfileImageUrl(getImageUrl(object.getString(KEY_PROFILE_IMAGE_PATH)));
                                crew.add(person);
                            }
                        } catch (JSONException e) {
                            Log.d("JSONArrayRequest", "getPeople: EXCEPTION OCCURRED");
                        }
                        if (callback != null) callback.processFinished(cast, crew);
                    }).start()
                    , error -> Log.d("JSONArrayRequest", "getPeople: ERROR OCCURRED"));
            ApplicationRequestHandler.getInstance().addToRequestQueue(request);

        }).start();
    }

    public void getLanguages(final SubcategoryListAsyncResponse callback) {
        new Thread(() -> {
            List<Subcategory> subcategoryList = new ArrayList<>();

            JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, getLanguagesUrl(), null,
                    response -> {
                        new Thread(() -> {
                            try {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject object = response.getJSONObject(i);
                                    subcategoryList.add(new Subcategory(object.getString(KEY_LANGUAGE_ISO_CODE), object.getString(KEY_ENGLISH_NAME)));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if (callback != null) callback.processFinished(subcategoryList);
                        }).start();
                    }, error -> {

            });
            ApplicationRequestHandler.getInstance().addToRequestQueue(request);
        }).start();
    }
}
