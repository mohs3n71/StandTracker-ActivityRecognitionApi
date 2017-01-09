package com.pouryazdan.mohsen.standtracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mohsen on 1/8/2017.
 */

public class DbHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "StandTracker";
    private static final String TABLE_STAND = "StandData";

    private static final String KEY_ID = "id";
    private static final String KEY_DATE = "date";
    private static final String KEY_COUNT = "count";

    public DbHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_STAND_TABLE = "CREATE TABLE " + TABLE_STAND + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_DATE + " TEXT," + KEY_COUNT + " TEXT" + ")";
        db.execSQL(CREATE_STAND_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STAND);
        onCreate(db);
    }

    public void addStandDate(StandData stand) {
        SQLiteDatabase db = this.getWritableDatabase("P@ssw0rd");
        ContentValues values = new ContentValues();
        values.put(KEY_DATE, stand.get_date());
        values.put(KEY_COUNT, stand.get_count());
        Log.e("error", values.get(KEY_DATE).toString() + " " + db.isOpen() + " " + values.get(KEY_COUNT).toString());
        try {
            db.insert(TABLE_STAND, null, values);
        } catch (Exception e) {
            Log.e("error", "Exception:" + e.getMessage());
        }
        db.close();
    }

    public StandData getStandDate(int id) {
        SQLiteDatabase db = this.getReadableDatabase("P@ssw0rd");

        Cursor cursor = db.query(TABLE_STAND, new String[]{KEY_ID, KEY_DATE, KEY_COUNT}, KEY_ID + " =?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        StandData stand = new StandData(Integer.parseInt(cursor.getString(0)), cursor.getString(1),
                Integer.parseInt(cursor.getString(2)));
        cursor.close();
        return stand;
    }

    public List<StandData> getAllStandDate() {
        List<StandData> standList = new ArrayList<StandData>();

        String select_query = "SELECT * FROM " + TABLE_STAND;
        SQLiteDatabase db = this.getReadableDatabase("P@ssw0rd");
        Cursor cursor = db.rawQuery(select_query, null);

        if (cursor.moveToFirst()) {
            do {
                StandData stand = new StandData(Integer.parseInt(cursor.getString(0)), cursor.getString(1),
                        Integer.parseInt(cursor.getString(2)));
                standList.add(stand);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return standList;
    }

    public int getStandDataCount() {
        String select_query = "SELECT * FROM " + TABLE_STAND;
        SQLiteDatabase db = this.getWritableDatabase("P@ssw0rd");

        Cursor cursor = db.rawQuery(select_query, null);
        int count = cursor.getCount();
        cursor.close();

        return count;
    }

    public void updateStandData(StandData stand) {
        SQLiteDatabase db = this.getWritableDatabase("P@ssw0rd");
        ContentValues values = new ContentValues();
        values.put(KEY_DATE, stand.get_date());
        values.put(KEY_COUNT, stand.get_count());

        db.update(TABLE_STAND, values, KEY_ID + " = ?", new String[]{String.valueOf(stand.get_id())});
    }

    public void deleteContact(StandData stand) {
        SQLiteDatabase db = this.getWritableDatabase("P@ssw0rd");
        db.delete(TABLE_STAND, KEY_ID + " = ? ", new String[]{String.valueOf(stand.get_id())});
        db.close();
    }

    public StandData findByDate(String date) {
        SQLiteDatabase db = this.getWritableDatabase("P@ssw0rd");
        String select_query = "SELECT * FROM " + TABLE_STAND + " WHERE " + KEY_DATE + " = \"" + date + "\"";
        try {

            Cursor cursor = db.rawQuery(select_query, null);
            Log.e("find", select_query);

            if (cursor.moveToFirst()) {
                StandData stand = new StandData(Integer.parseInt(cursor.getString(0)), cursor.getString(1),
                        Integer.parseInt(cursor.getString(2)));
                cursor.close();
                return stand;
            }
        } catch (Exception e) {
            Log.e("exception", e.getMessage());
        }
        return null;
    }
}
