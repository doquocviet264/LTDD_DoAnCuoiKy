package com.example.appdoan.Container.Request;

public class CategoryRequest {
    private String description;
    private String name;
    private String color;
    private Long type;

    public CategoryRequest(String description, String name, String color, Long type) {
        this.description = description;
        this.name = name;
        this.color = color;
        this.type = type;
    }
}
