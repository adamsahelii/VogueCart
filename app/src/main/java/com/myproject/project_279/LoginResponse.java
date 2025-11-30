package com.myproject.project_279;

public class LoginResponse {
    private boolean success;
    private int user_id;
    private String email;
    private String username;
    private String message;

    public boolean isSuccess() {
        return success;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getMessage() {
        return message;
    }
}
