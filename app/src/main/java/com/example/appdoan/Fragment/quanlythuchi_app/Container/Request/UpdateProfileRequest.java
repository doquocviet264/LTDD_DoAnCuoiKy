package com.example.appdoan.Fragment.quanlythuchi_app.Container.Request;

public class UpdateProfileRequest {
    private String firstname;
    private String lastname;
    private String avatar;

    public UpdateProfileRequest(String firstname, String lastname, String avatar) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.avatar = avatar;
    }
}
