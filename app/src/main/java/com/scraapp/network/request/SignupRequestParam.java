package com.scraapp.network.request;

import com.scraapp.network.ApiService;

public class SignupRequestParam extends RequestParam {

    private String email, password, mobile, name, action;

    public SignupRequestParam(String action, String userName, String email, String password, String mobile, ApiService apiService, String requestTag) {
        super(apiService, requestTag);

        name = userName;
        this.email = email;
        this.mobile = mobile;
        this.password = password;
        this.action = action;

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
