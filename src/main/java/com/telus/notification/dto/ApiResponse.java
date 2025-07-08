package com.telus.notification.dto;

public class ApiResponse<T> {
    private boolean success;
    private T data;
    private String message;
    private int statusCode;
    private String errors;
    private long timestamp;

    public ApiResponse(boolean success, T data, String message, int statusCode, String errors, long timestamp) {
        this.success = success;
        this.data = data;
        this.message = message;
        this.statusCode = statusCode;
        this.errors = errors;
        this.timestamp = timestamp;
    }

    // Constructor for backward compatibility
    public ApiResponse(boolean success, T data, String error) {
        this(success, data, null, success ? 200 : 400, error, System.currentTimeMillis());
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getErrors() {
        return errors;
    }

    public void setErrors(String errors) {
        this.errors = errors;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
