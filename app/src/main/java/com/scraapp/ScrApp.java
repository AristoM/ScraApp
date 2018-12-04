package com.scraapp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;

import com.scraapp.network.ApiClient;
import com.scraapp.network.request.RetroFitApp;

import org.greenrobot.eventbus.EventBus;

abstract public class ScrApp extends AppCompatActivity {

    private Handler mHandler;
    private ProgressDialog mDialog;

    protected EventBus mEventBus;
    protected ApiClient mApiClient;

    protected Context mContext;


    public abstract int getlayout();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(getlayout());

        mEventBus = EventBus.getDefault();
        mApiClient = getApp().getApiClient();
        mContext = this;

        if (!mEventBus.isRegistered(this)) {
            mEventBus.register(this);
        }
    }

    public RetroFitApp getApp() {
        return (RetroFitApp) getApplication();
    }

    public Context getContext() {
        return mContext;
    }

    @Override
    protected void onStop() {
        super.onStop();
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

}
