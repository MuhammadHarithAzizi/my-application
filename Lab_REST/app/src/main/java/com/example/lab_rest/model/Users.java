package com.example.lab_rest.model;

import java.sql.Timestamp;
import java.util.Date;

public class Users {

    // represent a record in users tables
    private int USER_ID;
    private String USER_EMAIL;
    private String USER_FULLNAME;
    private String USER_USERNAME;
    private String USER_PHONENUMBER;
    private String USER_PASSWORD;
    private String USER_CREATED_AT;
    private String USER_TOKEN;
    private String USER_LEASE;
    private String USER_ROLE;
    private int USER_IS_ACTIVE;
    private String USER_SECRET;

    //GETTER
    public int getUSER_ID() {
        return USER_ID;
    }
    public String getUSER_EMAIL() {
        return USER_EMAIL;
    }
    public String getUSER_FULLNAME() {
        return USER_FULLNAME;
    }
    public String getUSER_PHONENUMBER() { return USER_PHONENUMBER; }
    public String getUSER_USERNAME() {
        return USER_USERNAME;
    }
    public String getUSER_PASSWORD() {
        return USER_PASSWORD;
    }
    public String getUSER_CREATED_AT() {
        return USER_CREATED_AT;
    }
    public String getUSER_TOKEN() {
        return USER_TOKEN;
    }
    public String getUSER_LEASE() {
        return USER_LEASE;
    }
    public String getUSER_ROLE() {
        return USER_ROLE;
    }
    public int getUSER_IS_ACTIVE() {
        return USER_IS_ACTIVE;
    }
    public String getUSER_SECRET() {
        return USER_SECRET;
    }

    //SETTER
    public void setUSER_ID(int USER_ID) {
        this.USER_ID = USER_ID;
    }
    public void setUSER_EMAIL(String USER_EMAIL) {
        this.USER_EMAIL = USER_EMAIL;
    }
    public void setUSER_FULLNAME(String USER_FULLNAME) {
        this.USER_FULLNAME = USER_FULLNAME;
    }
    public void setUSER_PHONENUMBER(String USER_PHONENUMBER) { this.USER_PHONENUMBER = USER_PHONENUMBER; }
    public void setUSER_USERNAME(String USER_USERNAME) {
        this.USER_USERNAME = USER_USERNAME;
    }
    public void setUSER_PASSWORD(String USER_PASSWORD) {
        this.USER_PASSWORD = USER_PASSWORD;
    }
    public void setUSER_CREATED_AT(String USER_CREATED_AT) { this.USER_CREATED_AT = USER_CREATED_AT; }
    public void setUSER_TOKEN(String USER_TOKEN) {
        this.USER_TOKEN = USER_TOKEN;
    }
    public void setUSER_LEASE(String USER_LEASE) {
        this.USER_LEASE = USER_LEASE;
    }
    public void setUSER_ROLE(String USER_ROLE) {
        this.USER_ROLE = USER_ROLE;
    }
    public void setUSER_IS_ACTIVE(int USER_IS_ACTIVE) {
        this.USER_IS_ACTIVE = USER_IS_ACTIVE;
    }
    public void setUSER_SECRET(String USER_SECRET) {
        this.USER_SECRET = USER_SECRET;
    }
}
