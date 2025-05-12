package com.example.appdoan.Fragment.quanlythuchi_app.Model;

import android.app.Application;

public class GlobalVariable extends Application {
    private String appName = "com.example.quanlythuchi_app";
    private String accessToken;

    private UserModel userModel;

    public String getAccessToken() {
        return accessToken;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = "Bearer "+ accessToken;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }
}
