package com.example.dailyplanner.anxiliary;

public class User {
    private String id, firstName, password, email;
    boolean rememberMe;

    public User(){}

    public User(String firstName, String password, String email, boolean rememberMe) {
        this.firstName = firstName;
        this.password = password;
        this.rememberMe = rememberMe;
        this.email = email;
        this.id = "";
    }

    public User(String id, String firstName, String password, String email, boolean rememberMe) {
        this.firstName = firstName;
        this.password = password;
        this.rememberMe = rememberMe;
        this.email = email;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }
}
