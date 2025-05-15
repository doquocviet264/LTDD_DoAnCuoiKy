package com.example.appdoan.Container.Request;

public class BudgetRequest {

    private Long amount;

    private String description;



    public BudgetRequest( Long amount, String description) {
        this.amount = amount;

        this.description = description;
    }
}
