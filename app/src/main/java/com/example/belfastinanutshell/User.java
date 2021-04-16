package com.example.belfastinanutshell;

public class User {

    private String fullName, email;

    public User() {

    }

    public User(String fullName, String email) {
        this.fullName = fullName;
        this.email = email;
    }

    public String getDisplayName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = this.fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = this.email;
    }

}
