package com.example.simplemarketapplication.posts.post;

import java.util.Date;

public class PostUser {

    private String mUserId;
    private String mUserName;
    private String mPhoneNumber;
    private Date mUserCreatedTime;

    public PostUser() {}

    public PostUser(String userName, String phoneNumber) {
        this.mUserName = userName;
        this.mPhoneNumber = phoneNumber;
    }

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        this.mUserId = userId;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        this.mUserName = userName;
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.mPhoneNumber = phoneNumber;
    }

    public Date getUserCreatedTime() {
        return mUserCreatedTime;
    }

    public void setUserCreatedTime(Date userCreatedTime) {
        this.mUserCreatedTime = userCreatedTime;
    }
}
