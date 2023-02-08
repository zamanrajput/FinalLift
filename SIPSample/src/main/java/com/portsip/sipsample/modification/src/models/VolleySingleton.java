package com.portsip.sipsample.modification.src.models;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class VolleySingleton {
    public interface ResponseListener {
        void onResponse(JSONObject object);

        void onError(VolleyError error);
    }


    private static VolleySingleton mInstance;
    private static Context mContext;
    private static RequestQueue mRequestQueue;


    VolleySingleton(Context context) {
        if (mContext == null) mContext = context;
        mRequestQueue = getRequestQueue();
    }

    RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mContext);
        }
        return mRequestQueue;
    }

    public static synchronized VolleySingleton getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new VolleySingleton(context);
        }

        return mInstance;
    }




    public <T> void addToRequestQueue(Request<T> request) {
        getRequestQueue().add(request);

    }

}
