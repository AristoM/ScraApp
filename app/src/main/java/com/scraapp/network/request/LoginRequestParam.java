package com.scraapp.network.request;

public class LoginRequestParam {

    private String action;
    private String email;
    private String password;

    public LoginRequestParam(String action, String email, String password) {
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
