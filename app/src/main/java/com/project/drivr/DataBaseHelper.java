package com.project.drivr;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DataBaseHelper extends SQLiteOpenHelper {

    public DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    // onCreate method is called when the database is created for the first time.
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create your tables here using SQL commands
        String createTableQuery = "CREATE TABLE User(EMAIL TEXT PRIMARY KEY, FIRSTNAME TEXT, LASTNAME TEXT, GENDER TEXT, COUNTRY TEXT, CITY TEXT,PASSWORD TEXT, PHONE LONG);";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertUser(User user) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("EMAIL", user.getEmail());
        contentValues.put("FIRSTNAME", user.getFirstName());
        contentValues.put("LASTNAME", user.getLastName());
        contentValues.put("GENDER", user.getGender());
        contentValues.put("COUNTRY", user.getCountry());
        contentValues.put("CITY", user.getCity());
        contentValues.put("PASSWORD", user.getPassword());
        contentValues.put("PHONE", user.getPhoneNumber());
        sqLiteDatabase.insert("User", null, contentValues);
        sqLiteDatabase.close(); // Close the database to release resources
    }
    public Cursor getAllUsers() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM User", null);
    }
    public boolean isUserWithEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {"EMAIL"};
        String selection = "EMAIL" + " = ?";
        String[] selectionArgs = {email};
        Cursor cursor = db.query(
                "User",
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        boolean userExists = cursor != null && cursor.getCount() > 0;
        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return userExists;
    }
    public Cursor getUserByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] selectionArgs = { email };
        return db.rawQuery("SELECT * FROM User WHERE EMAIL=?", selectionArgs);
    }

}