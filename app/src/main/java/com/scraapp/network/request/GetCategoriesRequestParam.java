package com.scraapp.network.request;

import com.scraapp.network.ApiService;

public class GetCategoriesRequestParam extends RequestParam {

    private String action;

    public GetCategoriesRequestParam(ApiService apiService, String requestTag, String action) {
        super(apiService, requestTag);

        this.action = action;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
