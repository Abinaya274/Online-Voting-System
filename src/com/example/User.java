package com.example;

public class User {
    private final String username;
    private final String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public boolean hasPassword(String enteredPassword) {
        return password.equals(enteredPassword);
    }
}
