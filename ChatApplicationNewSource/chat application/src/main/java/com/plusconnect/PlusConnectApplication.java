package com.plusconnect;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by ambesh on 08-09-2015.
 */
public class PlusConnectApplication extends Application {

  private   RequestQueue queue;
    private Gson gson;
    @Override
    public void onCreate() {
        queue = Volley.newRequestQueue(this);
        GsonBuilder gsonBuilder=new GsonBuilder().serializeNulls();

        gson=gsonBuilder.create();

    }

    public Gson getGson() {

        return gson;
    }

    public RequestQueue getQueue() {
        return queue;
    }
}
