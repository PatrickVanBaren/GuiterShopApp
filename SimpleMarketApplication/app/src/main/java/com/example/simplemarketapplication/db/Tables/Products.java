package com.example.simplemarketapplication.db.Tables;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable(tableName = Products.TABLE_NAME)
public class Products {

    public static final String TABLE_NAME = "Products";
    public static final String COLUMN_NAME_ID = "_id";
    public static final String COLUMN_NAME_TITLE = "Title";
    public static final String COLUMN_NAME_PRICE = "Price";
    public static final String COLUMN_CREATE_TIME = "createdTime";

    @DatabaseField(id = true, columnName = COLUMN_NAME_ID)
    private String mId;
    @DatabaseField(columnName = COLUMN_NAME_TITLE)
    private String mTitle;
    @DatabaseField(columnName = COLUMN_NAME_PRICE)
    private String mPrice;
    @DatabaseField(columnName = COLUMN_CREATE_TIME, dataType = DataType.DATE_LONG)
    private Date mCreatedTime;

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
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

    public void setCreatedTime(final Date createdTime) {
        mCreatedTime = createdTime;
    }

//    public Map<String, Integer> product;
//    public Map<String, String> users;
//    public List<String> shoppingBasket;
//
//    public Products() {
//
//        product = new HashMap<String, Integer>(){{
//            put("Zombie V-165", 2500);
//            put("Zombie EDG-45", 100);
//            put("Ibanez Prestige RGD2127FX-ISH", 85000);
//            put("Ibanez AZ226PB-CBB Premium", 91645);
//            put("IBANEZ GIO GRG121DX-BKF BLACK FLAT", 19400);
//            put("IBANEZ RGA42FM-BLF", 38000);
//            put("LTD M-200 FM STBLK", 38151);
//            put("LTD MH-103QM STB", 35360);
//            put("ESP E-II HRF NT-8B BLACK SATIN", 198887);
//            put("ESP E-II T-B7 BARITONE BLKS", 167133);
//            put("Keisel D6G", 140526);
//            put("Keisel ZM7", 256926);
//            put("Legator N8FX", 136900);
//            put("Legator G8FX", 55400);
//            put("Legator Opus Performance 6", 26400);
//            put("Solar V1.7FB", 96100);
//            put("Solar S2.6C", 51700);
//        }};
//
//        users = new HashMap<>();
//
//        shoppingBasket = new ArrayList<>();
//    }



//    database.execSQL("CREATE TABLE '" + ContractUser.PRODUCT_TABLE + "' ('" +
//    ContractUser.COLUMN_NAME_ID + "' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, '" +
//    ContractUser.COLUMN_NAME_TITLE + "' VARCHAR, '" +
//    ContractUser.COLUMN_NAME_PRICE + "' INTEGER)");
//
//        database.execSQL("CREATE TABLE '" + ContractUser.USERS_TABLE + "' ('" +
//    ContractUser.COLUMN_NAME_ID + "' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, '" +
//    ContractUser.COLUMN_NAME_USER_NAME + "' VARCHAR, '" +
//    ContractUser.COLUMN_NAME_PHONE_NUMBER + "' VARCHAR)");
//
//        database.execSQL("CREATE TABLE '" + ContractUser.SHOPPING_BASKET_TABLE + "' ('" +
//    ContractUser.COLUMN_NAME_ID + "' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, '" +
//    ContractUser.COLUMN_NAME_ORDERS + "' VARCHAR)");
}
