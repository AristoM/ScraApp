package com.scraapp.network.request;

import com.scraapp.network.ApiService;

public class LoginRequestParam extends RequestParam {

    private String action;
    private String email;
    private String password;

    public LoginRequestParam(String action, String email, String password, ApiService apiService, String requestTag) {
        super(apiService, requestTag);
        this.action = action;
        this.email = email;
        this.password = password;
    }

    public String getmAction() {
        return action;
    }

    public void setmAction(String mAction) {
        this.action = mAction;
    }

    public String getmEmail() {
        return email;
    }

    public void setmEmail(String mEmail) {
        this.email = mEmail;
    }

    public String getmPassword() {
        return password;
    }

    public void setmPassword(String mPassword) {
        this.password = mPassword;
    }
}
