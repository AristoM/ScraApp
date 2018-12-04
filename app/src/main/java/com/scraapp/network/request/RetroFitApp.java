package com.scraapp.network.request;

import android.app.Application;

import com.scraapp.network.ApiClient;

public class RetroFitApp extends Application {

    private final String TAG = "RetroFitApp";
    private ApiClient mApiClient;
    private static RetroFitApp retroFitApp;

    @Override
    public void onCreate() {
        super.onCreate();
        retroFitApp = this;
    }

    public ApiClient getApiClient() {
        return mApiClient != null ? mApiClient : initApiClient();
    }

    /**
     * Initialize the {@link ApiClient}
     */
    private ApiClient initApiClient() {
        mApiClient = new ApiClient(this);
        return mApiClient;
    }

    public static RetroFitApp getApp() {
        return retroFitApp;
    }

}
