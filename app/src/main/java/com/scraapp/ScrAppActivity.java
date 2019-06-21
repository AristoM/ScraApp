package com.scraapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;

import com.scraapp.greendao.Categories;
import com.scraapp.greendao.CategoriesDao;
import com.scraapp.greendao.DaoSession;
import com.scraapp.mediators.BaseMediator;
import com.scraapp.network.ApiClient;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.greendao.query.Query;

abstract public class ScrAppActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private Handler mHandler;
    private ProgressDialog mDialog;

    protected EventBus mEventBus;
    protected ApiClient mApiClient;

    protected static Context mContext;

    protected BaseMediator baseMediator;

    protected CategoriesDao categoriesDao;
    protected Query<Categories> categoriesQuery;
    protected DaoSession daoSession;

    public abstract int getlayout();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(getlayout());

        mEventBus = EventBus.getDefault();
        mApiClient = getApp().getApiClient();
        mContext = this;

        if (!mEventBus.isRegistered(this)) {
            mEventBus.register(this);
        }

        initDAO();
    }

    private void initDAO() {
        // get the note DAO
        daoSession = ((ScrApp) getApplication()).getDaoSession();
        categoriesDao = daoSession.getCategoriesDao();

        // query all notes, sorted a-z by their text
//        categoriesQuery = categoriesDao.queryBuilder().orderAsc(CategoriesDao.Properties.Id).build();

    }

    public ScrApp getApp() {
        return (ScrApp) getApplication();
    }

    public Context getContext() {
        return mContext;
    }

    @Override
    protected void onStop() {
        super.onStop();
//        EventBus.getDefault().unregister(this);

        baseMediator = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * Initialize Loading Dialog
     */
    protected void initDialog(Context context) {
        this.mContext = context;
        mDialog = new ProgressDialog(mContext);
        mDialog.setMessage(getString(R.string.loading));

        mHandler = new Handler();
    }

    protected void dismissProgress() {
        if (mHandler != null && mDialog != null) {
            mHandler.post(() -> mDialog.dismiss());
        }
    }

    protected void showProgress() {
        if (mHandler != null && mDialog != null) {

            mHandler.post(() -> {
                if (!mDialog.isShowing()) {
                    mDialog.show();
                }
//        hideKeyboard(edt);
            });
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
