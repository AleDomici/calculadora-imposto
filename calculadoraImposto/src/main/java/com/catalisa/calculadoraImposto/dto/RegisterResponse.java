package com.catalisa.calculadoraImposto.dto;

public class RegisterResponse {
    private String message;
    private Long userId;
    private String username;

    public RegisterResponse(String message, Long userId, String username) {
        this.message = message;
        this.userId = userId;
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}