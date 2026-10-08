package com.threadloop.marketplace.dto;

public class AuthResponse {

    private boolean success;
    private String message;
    private UserDto user;
    private String token;
    private String tokenType = "Bearer";

    public AuthResponse() {}

    public AuthResponse(boolean success, String message, UserDto user) {
        this.success = success;
        this.message = message;
        this.user = user;
    }

    public AuthResponse(boolean success, String message, UserDto user, String token) {
        this.success = success;
        this.message = message;
        this.user = user;
        this.token = token;
        this.tokenType = "Bearer";
    }

    public static AuthResponse success(String message, UserDto user) {
        return new AuthResponse(true, message, user, null);
    }

    public static AuthResponse success(String message, UserDto user, String token) {
        return new AuthResponse(true, message, user, token);
    }

    public static AuthResponse error(String message) {
        return new AuthResponse(false, message, null, null);
    }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public UserDto getUser() { return user; }
    public void setUser(UserDto user) { this.user = user; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getTokenType() { return tokenType; }
    public void setTokenType(String tokenType) { this.tokenType = tokenType; }
}
