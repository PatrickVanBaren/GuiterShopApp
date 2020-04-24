package com.example.simplemarketapplication.posts.post;

import com.example.simplemarketapplication.db.Tables.Users;

import java.util.Date;

public class PostShoppingBasket {

    private String mSBId;
    private String mProductDescription;
    private String mProductPrice;
    private String mUserName;
    private String mUserPhone;
    private Date mCreateTime;
    private static Boolean isProductPurchased = false;

    public PostShoppingBasket(){}
    public PostShoppingBasket(PostProduct product) {
        mProductDescription = product.getDescription();
        mProductPrice = product.getPrice();
        isProductPurchased = false;
    }
    public PostShoppingBasket(PostProduct product, Users user) {
        mProductDescription = product.getDescription();
        mProductPrice = product.getPrice();
        mUserName = user.getUserName();
        mUserPhone = user.getPhoneNumber();
        isProductPurchased = true;
    }

    public String getSBId() {
        return mSBId;
    }

    public void setSBId(String mAdminSBId) {
        this.mSBId = mAdminSBId;
    }

    public String getProductDescription() {
        return mProductDescription;
    }

    public void setProductDescription(String mProductDescription) {
        this.mProductDescription = mProductDescription;
    }

    public String getProductPrice() {
        return mProductPrice;
    }

    public void setProductPrice(String mProductPrice) {
        this.mProductPrice = mProductPrice;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String mUserName) {
        this.mUserName = mUserName;
    }

    public String getUserPhone() {
        return mUserPhone;
    }

    public void setUserPhone(String mUserPhone) {
        this.mUserPhone = mUserPhone;
    }

    public Date getCreateTime() {
        return mCreateTime;
    }

    public void setCreateTime(Date mCreateTime) {
        this.mCreateTime = mCreateTime;
    }
}
