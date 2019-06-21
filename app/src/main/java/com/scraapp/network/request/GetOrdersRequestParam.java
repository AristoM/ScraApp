package com.scraapp.network.request;

import com.scraapp.network.ApiService;

public class GetOrdersRequestParam extends RequestParam {

    private String action;
    private String user_id;

    public GetOrdersRequestParam(String action, String userId, ApiService apiService, String requestTag) {
        super(apiService, requestTag);
        this.action = action;
        this.user_id = userId;
    }
}
