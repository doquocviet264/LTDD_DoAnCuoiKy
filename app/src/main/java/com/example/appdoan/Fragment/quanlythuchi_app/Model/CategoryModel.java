package com.example.appdoan.Fragment.quanlythuchi_app.Model;

import java.io.Serializable;

public class CategoryModel implements Serializable {
    private Long id;

    private String description;

    private String name;

    private String color;

    private Long type;


    public CategoryModel(Long id, String description, String name, String color, Long type) {
        this.id = id;
        this.description = description;
        this.name = name;
        this.color = color;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

}
