package com.example.appdoan.Container.Request;

public class GoalRequest {
    private String name;

    private Long balance;

    private Long amount;

    private String deadline;

    public GoalRequest(String name,Long balance,Long amount,String deadline){
        this.name=name;
        this.balance=balance;
        this.amount=amount;
        this.deadline=deadline;
    }
}
