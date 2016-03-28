package com.example.bowenshi.intersaclay;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by qli on 07/03/16.
 */
public class GlobalRequestQueue {
    private static GlobalRequestQueue instance;
    private RequestQueue requestQueue;
    private static Context ctx;

    private GlobalRequestQueue(Context ctx) {
        this.ctx = ctx;
        this.requestQueue = getRequestQueue();
    }

    public static synchronized GlobalRequestQueue getInstance(Context ctx) {
        if (instance == null) {
            instance = new GlobalRequestQueue(ctx);
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (this.requestQueue == null) {
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}
