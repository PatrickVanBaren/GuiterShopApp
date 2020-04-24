package com.example.simplemarketapplication.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.simplemarketapplication.db.Tables.Products;
import com.example.simplemarketapplication.db.Tables.ShoppingBasket;
import com.example.simplemarketapplication.db.Tables.Users;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.List;

import androidx.annotation.Nullable;

public class Database {

    private static Database mInstance;
    private final DatabaseHelper mDatabaseHelper;

    public static void createInstance(Context context) {
        mInstance = new Database(context);
    }

    public static Database getInstance() {
        return mInstance;
    }

    private Database(@Nullable Context context) {
        mDatabaseHelper = new DatabaseHelper(context);
    }

    //get tables list
    public List<Products> getProductsList() throws SQLException {
        return mDatabaseHelper.getDao(Products.class).queryForAll();
    }

    public List<Users> getUsersList() throws SQLException {
        return mDatabaseHelper.getDao(Users.class).queryForAll();
    }

    public List<ShoppingBasket> getShoppingBasketList() throws SQLException {
        return mDatabaseHelper.getDao(ShoppingBasket.class).queryForAll();
    }

    //create rows
    public void createProduct(Products product) throws SQLException {
        mDatabaseHelper.getDao(Products.class).create(product);
    }

    public void createUser(Users user) throws SQLException {
        mDatabaseHelper.getDao(Users.class).create(user);
    }

    public void createShoppingBasket(ShoppingBasket shoppingBasket) throws SQLException {
        mDatabaseHelper.getDao(ShoppingBasket.class).create(shoppingBasket);
    }

    //update tables
    public void updateProducts(Products product) throws SQLException {
        mDatabaseHelper.getDao(Products.class).update(product);
    }

    public void updateUsers(Users user) throws SQLException {
        mDatabaseHelper.getDao(Users.class).update(user);
    }

    public void updateShoppingBasket(ShoppingBasket shoppingBasket) throws SQLException {
        mDatabaseHelper.getDao(ShoppingBasket.class).update(shoppingBasket);
    }

    //delete rows
    public void deleteProduct(String id) throws SQLException {
        final Dao<Products, String> dao = mDatabaseHelper.getDao(Products.class);
        dao.deleteById(id);
    }

    public void deleteProductSB(String id) throws SQLException {
        final Dao<ShoppingBasket, String> dao = mDatabaseHelper.getDao(ShoppingBasket.class);
        dao.deleteById(id);
    }

    public void deleteUser(String id) throws SQLException {
        final Dao<Users, String> dao = mDatabaseHelper.getDao(Users.class);
        dao.deleteById(id);
    }

    public void deleteAllProducts() throws SQLException {
        mDatabaseHelper.getDao(Products.class).deleteBuilder().delete();
    }

    public void deleteAllUsers() throws SQLException {
        mDatabaseHelper.getDao(Users.class).deleteBuilder().delete();
    }

    private static class DatabaseHelper extends OrmLiteSqliteOpenHelper {

        private static final String NAME = "MarketDatabase.mdb";
        private static final int VERSION = 1;

        private Products productsDAO = null;
        private ShoppingBasket basketDAO = null;
        private Users usersDAO = null;

        public DatabaseHelper(Context context) {
            super(context, NAME, null, VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
            try {
                TableUtils.createTable(connectionSource, Products.class);
                TableUtils.createTable(connectionSource, Users.class);
                TableUtils.createTable(connectionSource, ShoppingBasket.class);
            } catch (SQLException e) {
                throw new RuntimeException("Не удалось загрузить базу данных! " + e);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
            try {
                TableUtils.dropTable(connectionSource, Products.class, true);
                TableUtils.dropTable(connectionSource, Users.class, true);
                TableUtils.dropTable(connectionSource, ShoppingBasket.class, true);

                TableUtils.createTable(connectionSource, Products.class);
                TableUtils.createTable(connectionSource, Users.class);
                TableUtils.createTable(connectionSource, ShoppingBasket.class);
            } catch (SQLException e) {
                throw new RuntimeException("Не удалось обновить базу данных! " + e);
            }
        }
    }
}
