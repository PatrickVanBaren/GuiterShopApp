package com.example.simplemarketapplication.db.Tables;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable(tableName = Users.TABLE_NAME)
public class Users {

    public static final String TABLE_NAME = "Users";
    public static final String COLUMN_NAME_ID = "_id";
    public static final String COLUMN_NAME_USER_NAME = "Name";
    public static final String COLUMN_NAME_PHONE_NUMBER = "Number";
    public static final String COLUMN_NAME_CREATE_DATE = "Date";

    @DatabaseField(id = true, columnName = COLUMN_NAME_ID)
    private String mId;
    @DatabaseField(columnName = COLUMN_NAME_USER_NAME)
    private String mUserName;
    @DatabaseField(columnName = COLUMN_NAME_PHONE_NUMBER)
    private String mPhoneNumber;
    @DatabaseField(columnName = COLUMN_NAME_CREATE_DATE)
    private Date mCreateTime;

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String mUserName) {
        this.mUserName = mUserName;
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public void setPhoneNumber(String mPhoneNumber) {
        this.mPhoneNumber = mPhoneNumber;
    }

    public Date getCreateTime() {
        return mCreateTime;
    }

    public void setCreateTime(Date mCreateTime) {
        this.mCreateTime = mCreateTime;
    }
}
