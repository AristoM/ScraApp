package com.scraapp;

import android.app.Application;

import com.scraapp.greendao.DaoMaster;
import com.scraapp.greendao.DaoSession;
import com.scraapp.network.ApiClient;

import org.greenrobot.greendao.database.Database;

public class ScrApp extends Application {

    private final String TAG = "ScrApp";
    private ApiClient mApiClient;
    private static ScrApp scrApp;

    private DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        scrApp = this;

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "categories-db");
        Database db = helper.getWritableDb();

        daoSession = new DaoMaster(db).newSession();

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

    public static ScrApp getApp() {
        return scrApp;
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

}
