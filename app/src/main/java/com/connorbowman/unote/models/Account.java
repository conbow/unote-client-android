package com.connorbowman.unote.models;

public class Account {

    private String email;
    private String password;
    private String name;

    public Account(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getName() { return name; }
}
