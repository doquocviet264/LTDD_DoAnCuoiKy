package com.example.appdoan.Model;


import java.io.Serializable;

public class CardModel implements Serializable {

    private Long id;

    private String name;

    private Long balance;

    private String cardnumber;

    private String description;

    public CardModel(){}
    public CardModel(Long id, String name, Long balance, String cardnumber, String description) {
        this.id = id;
        this.name = name;
        this.balance = balance;
        this.cardnumber = cardnumber;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    public String getCardnumber() {
        return cardnumber;
    }

    public void setCardnumber(String cardnumber) {
        this.cardnumber = cardnumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

