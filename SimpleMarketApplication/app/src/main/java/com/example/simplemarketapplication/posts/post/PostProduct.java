package com.example.simplemarketapplication.posts.post;

import java.util.Date;

public class PostProduct {

    private String mId;
    private String mDescription;
    private String mPrice;
    private Date mCreatedTime;

    public PostProduct() {}
    public PostProduct(String productName, String price) {
        mDescription = productName;
        mPrice = price;
    }

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getPrice() {
        return mPrice;
    }

    public void setPrice(String mPrice) {
        this.mPrice = mPrice;
    }

    public Date getCreatedTime() {
        return mCreatedTime;
    }

    public void setCreatedTime(Date createdTime) {
        mCreatedTime = createdTime;
    }
}
