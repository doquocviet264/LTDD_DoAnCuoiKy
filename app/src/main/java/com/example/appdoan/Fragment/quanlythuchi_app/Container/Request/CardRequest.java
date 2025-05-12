package com.example.appdoan.Fragment.quanlythuchi_app.Container.Request;

public class CardRequest {
    private String name;

    private Long balance;

    private String cardnumber;

    private String description;

    public CardRequest(String name, Long balance, String cardnumber, String description) {
        this.name = name;
        this.balance = balance;
        this.cardnumber = cardnumber;
        this.description = description;
    }
}
