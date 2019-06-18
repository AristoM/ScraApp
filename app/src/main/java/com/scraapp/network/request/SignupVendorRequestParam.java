package com.scraapp.network.request;

import com.scraapp.network.ApiService;

public class SignupVendorRequestParam extends RequestParam {

    String action;
    String address;
    String city;
    String close_time;
    String desc;
    String email;
    String lat;
    String lon;
    String location;
    String name;
    String open_time;
    String phone;
    String website;
    String password;


    SignupVendorRequestParam(String action, String address, String city, String  email, ApiService apiService, String requestTag) {
        super(apiService, requestTag);
    }
}
