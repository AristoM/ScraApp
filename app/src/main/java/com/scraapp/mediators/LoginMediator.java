package com.scraapp.mediators;

public interface LoginMediator {

    public Long getLogin();

    public interface LoginInterface {
        public void onLoginSuccess();
        public void onLoginError();
    }

}
