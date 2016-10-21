package com.dsz.asqlitetest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Soham on 17-Mar-15.
 */
public class RestaurantDataSource {
    // Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_NAME, MySQLiteHelper.COLUMN_ADDRESS,
            MySQLiteHelper.COLUMN_TYPE };

    public RestaurantDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public RestaurantModel createRestaurant(String name, String address, int type) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_NAME, name);
        values.put(MySQLiteHelper.COLUMN_ADDRESS, address);
        values.put(MySQLiteHelper.COLUMN_TYPE, type);
        long insertId = database.insert(MySQLiteHelper.TABLE_RESTAURANTS, null,
                values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_RESTAURANTS,
                allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        RestaurantModel newRestaurant = cursorToRestaurant(cursor);
        cursor.close();
        return newRestaurant;
    }

    public void deleteRestaurant(RestaurantModel restaurant) {
        long id = restaurant.getId();
        System.out.println("Restaurant deleted with id: " + id);
        database.delete(MySQLiteHelper.TABLE_RESTAURANTS, MySQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }

    public void updateRestaurant(RestaurantModel restaurant) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_NAME, restaurant.getName());
        values.put(MySQLiteHelper.COLUMN_ADDRESS, restaurant.getAddress());
        values.put(MySQLiteHelper.COLUMN_TYPE, restaurant.getType());
        long id = restaurant.getId();
        System.out.println("Restaurant updated with id: " + id);
        database.update(MySQLiteHelper.TABLE_RESTAURANTS, values,
                MySQLiteHelper.COLUMN_ID + " = ?", new String[]
                        { String.valueOf(restaurant.getId()) });
    }

    public List<RestaurantModel> getAllRestaurants() {
        List<RestaurantModel> restaurants = new ArrayList<RestaurantModel>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_RESTAURANTS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            RestaurantModel restaurant = cursorToRestaurant(cursor);
            restaurants.add(restaurant);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return restaurants;
    }

    private RestaurantModel cursorToRestaurant(Cursor cursor) {
        RestaurantModel restaurant = new RestaurantModel();
        restaurant.setId(cursor.getLong(0));
        restaurant.setName(cursor.getString(1));
        restaurant.setAddress(cursor.getString(2));
        restaurant.setType(cursor.getInt(3));
        return restaurant;
    }
}
