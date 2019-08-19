package com.scraapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.scraapp.network.event.ApiErrorEvent;
import com.scraapp.network.event.ApiErrorWithMessageEvent;
import com.scraapp.network.response.AbstractApiResponse;
import com.scraapp.utility.Constant;

import org.greenrobot.eventbus.Subscribe;

public class MyOrdersActivity extends ClickAwareActivity {

    @Override
    public int getlayout() {
        return R.layout.myorders_activity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle(R.string.my_orders);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

//        GetOrdersRequestParam getOrdersRequestParam = new GetOrdersRequestParam(ActionRequest.GET_ORDERS, "1", null, Constant.GET_ORDERS_REQUEST_TAG);
//        mApiClient.getOrdersRequest(getOrdersRequestParam);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }


    /**
     * Response of Uploaded File
     *
     * @param apiResponse UploadFileResponse
     */
    @Subscribe
    public void onEventMainThread(AbstractApiResponse apiResponse) {
        switch (apiResponse.getRequestTag()) {
            case Constant.GET_ORDERS_REQUEST_TAG:
                dismissProgress();
                CommonUtils.displayToast(getContext(), apiResponse.getMessage());

                break;

            default:
                break;
        }
    }


    /**
     * EventBus listener. An API call failed. No error message was returned.
     *
     * @param event ApiErrorEvent
     */
    @Subscribe
    public void onEventMainThread(ApiErrorEvent event) {
        switch (event.getRequestTag()) {
            case Constant.GET_ORDERS_REQUEST_TAG:
                dismissProgress();
                CommonUtils.displayToast(getContext(), event.getRetrofitError().toString());
                Log.e("okhttp", event.getRetrofitError().toString());
                break;

            default:
                break;
        }
    }


    /**
     * EventBus listener. An API call failed. An error message was returned.
     *
     * @param event ApiErrorWithMessageEvent Contains the error message.
     */
    @Subscribe
    public void onEventMainThread(ApiErrorWithMessageEvent event) {
        switch (event.getRequestTag()) {
            case Constant.GET_ORDERS_REQUEST_TAG:
                dismissProgress();
                CommonUtils.displayToast(getContext(), event.getResultMsgUser());
                break;

            default:
                break;
        }
    }

}
