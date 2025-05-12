package com.example.appdoan.Fragment.quanlythuchi_app.Model;

import java.io.Serializable;

public class TransactionModel implements Serializable {
    private Long id;

    private CategoryModel categoryModel;

    private CardModel cardModel;

    private String name;

    private Long amount;

    private String location;

    private String transactiondate;

    private Long type;

    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CategoryModel getCategoryModel() {
        return categoryModel;
    }

    public void setCategoryModel(CategoryModel categoryModel) {
        this.categoryModel = categoryModel;
    }

    public CardModel getCardModel() {
        return cardModel;
    }

    public void setCardModel(CardModel cardModel) {
        this.cardModel = cardModel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTransactiondate() {
        return transactiondate;
    }

    public void setTransactiondate(String transactiondate) {
        this.transactiondate = transactiondate;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TransactionModel(Long id, CategoryModel categoryModel, CardModel cardModel, String name, Long amount, String location, String transactiondate, Long type, String description) {
        this.id = id;
        this.categoryModel = categoryModel;
        this.cardModel = cardModel;
        this.name = name;
        this.amount = amount;
        this.location = location;
        this.transactiondate = transactiondate;
        this.type = type;
        this.description = description;
    }
}
