package com.burtech.fixmycar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class CarDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "/data/data/com.burtech.fixmycar/databases/myCars.db";
    private static final String TABLE_CarParts = "CarParts";

    private static final String FIELD_CAR_PART = "CARPART";
    private static final String FIELD_PRICE = "PRICE";
    private static final String FIELD_DURATION = "DURATION";
    private static final int DATABASE_VERSION = 1;

    CarDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_CarParts + "(_id integer PRIMARY KEY," + FIELD_CAR_PART + " TEXT, " + FIELD_PRICE + " INTEGER, " + FIELD_DURATION + " INTEGER );");
        Log.i("onCreate", "onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Handle database upgrade as needed
    }

    public boolean saveRecord(String carPart, Integer carPrice, Integer Duration) {
        try
        {
            long id = findCarPartID(carPart);
            if (id>0)
            {
                updateRecord(id, carPart, carPrice, Duration);
                return false;
            }
            else
            {
                addRecord(carPart, carPrice, Duration);
                return true;
            }
        }
        catch (SQLException ex)
        {
            Log.i("findCarPartID", ex.getMessage());
            return false;
        }
    }

    public long addRecord(String carPart, Integer carPrice, Integer Duration) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FIELD_CAR_PART, carPart);
        values.put(FIELD_PRICE, carPrice);
        values.put(FIELD_DURATION, Duration);
        return db.insert(TABLE_CarParts, null, values);
    }

    public int updateRecord(long id, String carPart, Integer carPrice, Integer Duration) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("_id", id);
        values.put(FIELD_CAR_PART, carPart);
        values.put(FIELD_PRICE, carPrice);
        values.put(FIELD_DURATION, Duration);
        return db.update(TABLE_CarParts, values, "_id = ?", new String[]{String.valueOf(id)});
    }

    public int deleteRecord(long id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE_CarParts, "_id = ?", new String[]{String.valueOf(id)});
    }

    public long findCarPartID(String carPart) {
        long returnVal = -1;
        SQLiteDatabase db = getReadableDatabase();
        try
        {
            String query = "SELECT _id FROM " + TABLE_CarParts + " WHERE " + FIELD_CAR_PART + " = " + "\'" +  carPart + "\'";
            Cursor cursor = db.rawQuery(query, null);
            Log.i("findCarPartID", "getCount()=" + cursor.getCount());

            if (cursor.getCount() > 0)
            {
                cursor.moveToFirst();
                returnVal = cursor.getInt(0);
            }
            return returnVal;
        }
        catch (SQLException ex)
        {
            Log.i("findCarPartID", ex.getMessage());
            return -1;
        }
    }

    public Cursor getCarPartsList() {
        SQLiteDatabase db = getReadableDatabase();
        try
        {
            String query = "SELECT _id, " + FIELD_CAR_PART + ", " + FIELD_PRICE + ", "  + FIELD_DURATION  + " FROM " + TABLE_CarParts + " ORDER BY " + FIELD_CAR_PART + " ASC";;
            return db.rawQuery(query, null);
        }
        catch (SQLException ex)
        {
            Log.i("CarDatabase", ex.getMessage());
            return null;
        }
    }
}
