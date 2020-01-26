package com.example.moviesearcher.model.handlers;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class ApplicationRequestHandler extends Application {

    public final String TAG = ApplicationRequestHandler.class.getSimpleName();
    private static ApplicationRequestHandler instance;
    private RequestQueue requestQueue;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
    public static synchronized ApplicationRequestHandler getInstance(){ return instance; }

    public RequestQueue getRequestQueue(){
        if (requestQueue == null)
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> request, String tag){
        request.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(request);
    }

    public <T> void addToRequestQueue(Request<T> request){
        request.setTag(TAG);
        getRequestQueue().add(request);
    }
}
