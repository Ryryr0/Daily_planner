package com.example.dailyplanner;

public class User {
    private String lastName, firstName, password;

    public User(String lastName, String firstName, String password) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.password = password;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getPassword() {
        return password;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
