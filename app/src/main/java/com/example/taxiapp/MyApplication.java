package com.example.taxiapp;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import taxiapp.utils.AppPreferences;
import taxiapp.utils.LruBitmapCache;


@ReportsCrashes(formKey = "",
    mailTo = "hassanjamil91@gmail.com",
    mode = ReportingInteractionMode.TOAST,
    forceCloseDialogAfterToast = false,
    resToastText = R.string.crash_toast_text
)
public class MyApplication extends Application {

    private static final String TAG = MyApplication.class.getSimpleName();

    private static MyApplication mInstance;

    // Volley (HTTP request handling library)
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    // Application Preferences
    private AppPreferences appPreferences;


    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;

        // Initiation
        ACRA.init(this);

        appPreferences = new AppPreferences(this);
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    // Functions supported by volley library
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }
    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }
    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }
    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }



    public AppPreferences getAppPreferences() {
        return this.appPreferences;
    }
}
