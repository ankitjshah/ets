package com.msd.finalproject.model;

/**
 * User Model to manage user's login details
 */
public class User {
    private int id;

    private String userName;

    private String password;

    public User() {
        this.id = 0;
        this.userName = null;
        this.password = null;
    }

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public User(int id, String userName, String password) {
        this.id = id;
        this.userName = userName;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
