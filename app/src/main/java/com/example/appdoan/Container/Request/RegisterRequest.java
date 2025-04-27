package com.example.appdoan.Container.Request;

public class RegisterRequest {
    private String userName;

    private String passWord;

    private String firstName;

    private String lastName;

    private String email;

    private String roleName;

    private String avatar;

    public RegisterRequest(String userName, String passWord, String firstName, String lastName, String email, String roleName, String avatar) {
        this.userName = userName;
        this.passWord = passWord;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.roleName = roleName;
        this.avatar = avatar;
    }
}
