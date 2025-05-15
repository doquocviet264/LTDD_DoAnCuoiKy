package com.example.appdoan.Model;


import java.io.Serializable;

public class NotificationModel implements Serializable {
    private Long id;

    private String title;

    private String content;

    private boolean is_read;

    private String created_at;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isIs_read() {
        return is_read;
    }

    public void setIs_read(boolean is_read) {
        this.is_read = is_read;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public NotificationModel(Long id, String title, String content, boolean is_read, String created_at) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.is_read = is_read;
        this.created_at = created_at;
    }
}
