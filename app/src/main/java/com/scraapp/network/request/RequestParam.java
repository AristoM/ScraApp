package com.scraapp.network.request;

import com.scraapp.network.ApiService;

public class RequestParam {

    private ApiService mApiService;
    private String mRequestTag;

    RequestParam(ApiService apiService, String requestTag){
        mApiService = apiService;
        mRequestTag = requestTag;
    }

    public ApiService getmApiService() {
        return mApiService;
    }

    public void setmApiService(ApiService mApiService) {
        this.mApiService = mApiService;
    }

    public String getmRequestTag() {
        return mRequestTag;
    }

    public void setmRequestTag(String mRequestTag) {
        this.mRequestTag = mRequestTag;
    }
}
