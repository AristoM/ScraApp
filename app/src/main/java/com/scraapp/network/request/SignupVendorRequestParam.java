package com.scraapp.network.request;

import com.scraapp.network.ApiService;

public class SignupVendorRequestParam extends RequestParam {

    private String action;
    private String address;
    private String city;
    String close_time;
    String desc;
    private String email;
    private String lat;
    String lon;
    String location;
    String name;
    String open_time;
    String phone;
    String website;
    String password;
    String pan;
    String adhar;


    public SignupVendorRequestParam(String action, String address, String city, String email, String lat, String lon, String location, String name,
                                    String phone, String website, String password, String pan, String adhar, ApiService apiService, String requestTag) {
        super(apiService, requestTag);
        this.action = action;
        this.address = address;
        this.city = city;
        this.email = email;
        this.lat = lat;
        this.lon = lon;
        this.location = location;
        this.name = name;
        this.phone = phone;
        this.website = website;
        this.password = password;
        this.pan = pan;
        this.adhar = adhar;
    }


}
