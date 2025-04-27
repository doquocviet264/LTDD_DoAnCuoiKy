package com.example.appdoan.Container.Response;


public class ApiResponse<T> {

    private int status;
    private int error;
    private String message;
    private T data;

    // Constructor với các giá trị mặc định
    public ApiResponse() {
        this.status = 200;
        this.error = 0;
        this.message = null;
    }

    // Getter và setter
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}

