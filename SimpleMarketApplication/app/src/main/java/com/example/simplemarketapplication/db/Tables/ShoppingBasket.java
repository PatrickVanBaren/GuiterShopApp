package com.example.simplemarketapplication.db.Tables;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable(tableName = ShoppingBasket.TABLE_NAME)
public class ShoppingBasket {

    public static final String TABLE_NAME = "Shopping basket";
    public static final String COLUMN_NAME_ID = "_id";
    public static final String COLUMN_NAME_PRODUCT = "Product";
    public static final String COLUMN_NAME_PRICE = "Price";
    public static final String COLUMN_NAME_USER = "User";
    public static final String COLUMN_NAME_PHONE = "Phone";
    public static final String COLUMN_CREATE_TIME = "createdTime";

    @DatabaseField(id = true, columnName = COLUMN_NAME_ID)
    private String mId;
    @DatabaseField(columnName = COLUMN_NAME_PRODUCT)
    private String mProduct;
    @DatabaseField(columnName = COLUMN_NAME_PRICE)
    private String mPrice;
    @DatabaseField(columnName = COLUMN_NAME_USER)
    private String mUser;
    @DatabaseField(columnName = COLUMN_NAME_PHONE)
    private String mPhone;
    @DatabaseField(columnName = COLUMN_CREATE_TIME, dataType = DataType.DATE_LONG)
    private Date mCreatedTime;

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public String getTitle() {
        return mProduct;
    }

    public void setTitle(String mTitle) {
        this.mProduct = mTitle;
    }

    public String getPrice() {
        return mPrice;
    }

    public String getUser() {
        return mUser;
    }

    public void setUser(String mUser) {
        this.mUser = mUser;
    }

    public String getPhone() {
        return mPhone;
    }

    public void setPhone(String mPhone) {
        this.mPhone = mPhone;
    }

    public void setPrice(String mPrice) {
        this.mPrice = mPrice;
    }

    public Date getCreatedTime() {
        return mCreatedTime;
    }

    public void setCreatedTime(final Date createdTime) {
        mCreatedTime = createdTime;
    }
}
