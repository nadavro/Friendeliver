package com.nadavrozen.myfirebaseauth;

/**
 * Created by Israel Rozen on 05/09/2016.
 */

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;



public class MyApplication extends Application{
    private RequestQueue mRequestQueue;
    private static MyApplication mInstance;
    public static final String TAG = MyApplication.class
            .getSimpleName();
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }
    public static synchronized MyApplication getInstance() {
        return mInstance;
    }
    public RequestQueue getReqQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }
    public <T> void addToReqQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getReqQueue().add(req);
    }
    public <T> void addToReqQueue(Request<T> req) {
        req.setTag(TAG);
        getReqQueue().add(req);
    }
    public void cancelPendingReq(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
