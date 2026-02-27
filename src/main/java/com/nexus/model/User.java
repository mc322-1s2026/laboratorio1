package com.nexus.model;

public class User {
    private final String username;
    private final String email;

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    // O compilador reclamou da falta deste método:
    public String consultEmail() {
        return email;
    }

    public String consultUsername() {
        return username;
    }

    public long calculateWorkload() {
        return 0; 
    }
}