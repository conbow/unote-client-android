package com.connorbowman.unote.models;

public class Session {

    private String name;
    private String email;
    private String password;
    private String sessionToken;

    public Session(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Session(String sessionToken, String email, String name) {
        this.sessionToken = sessionToken;
        this.email = email;
        this.name = name;
    }

    public String getToken() {
        return sessionToken;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }
}
