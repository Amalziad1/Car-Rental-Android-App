package com.project.drivr;
import com.project.drivr.ui.reservations.ReservationsFragment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.project.drivr.ui.car_menu.Car;
import com.project.drivr.ui.reservations.Reservation;

import java.text.SimpleDateFormat;
import java.util.Locale;


public class DataBaseHelper extends SQLiteOpenHelper {
    private static DataBaseHelper instance = null;
    public static DataBaseHelper getInstance(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        if (instance != null) {
            return instance;
        }
        instance = new DataBaseHelper(context, name, factory, version);
        return instance;
    }
    public DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    // onCreate method is called when the database is created for the first time.
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableUser= "CREATE TABLE IF NOT EXISTS User(EMAIL TEXT PRIMARY KEY, FIRSTNAME TEXT, LASTNAME TEXT, GENDER TEXT, COUNTRY TEXT, CITY TEXT,PASSWORD TEXT, PHONE LONG);";
        db.execSQL(createTableUser);
        String createTableCar = "CREATE TABLE IF NOT EXISTS Car(VIN TEXT PRIMARY KEY, FACTORY TEXT, TYPE TEXT, PRICE DOUBLE, MODEL INTEGER, IMG TEXT);";
        db.execSQL(createTableCar);
        String createTableReservation = "CREATE TABLE IF NOT EXISTS Reserve(ID INTEGER PRIMARY KEY AUTOINCREMENT, DATE_RESERVED DATE, TIME_RESERVED TIME, VIN TEXT, EMAIL TEXT, FOREIGN KEY(VIN) REFERENCES Car(VIN),FOREIGN KEY(EMAIL) REFERENCES User(EMAIL) );";
        db.execSQL(createTableReservation);
        String createTableFavorites = "CREATE TABLE IF NOT EXISTS Favorite( TIMED TIME,VIN TEXT, EMAIL TEXT, FOREIGN KEY(VIN) REFERENCES Car(VIN),FOREIGN KEY(EMAIL) REFERENCES User(EMAIL),PRIMARY KEY (VIN, EMAIL));";
        db.execSQL(createTableFavorites);
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
        sqLiteDatabase.close();
    }
    public void insertCar(Car car) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("VIN", car.getVIN());
        contentValues.put("FACTORY", car.getFactory());
        contentValues.put("TYPE", car.getType());
        contentValues.put("PRICE", car.getPrice());
        contentValues.put("MODEL", car.getModel());
        contentValues.put("IMG", car.getImgURL());
        sqLiteDatabase.insert("Car", null, contentValues);
        sqLiteDatabase.close();
    }
    public void insertReservation(Reservation reserve) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //convert Date and Time to formatted strings
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        values.put("DATE_RESERVED", dateFormat.format(reserve.getDate()));
        values.put("TIME_RESERVED", timeFormat.format(reserve.getTime()));
        values.put("VIN", reserve.getVIN());
        values.put("EMAIL", reserve.getEmail());
        db.insert("Reserve", null, values);
        db.close();
    }
    public void insertFavorite(String email,String VIN) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("EMAIL", email);
        contentValues.put("VIN", VIN);
        sqLiteDatabase.insert("Favorite", null, contentValues);
        sqLiteDatabase.close();
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
    public Cursor getReservation(String email){
        SQLiteDatabase db = this.getReadableDatabase();
        String[] selectionArgs = { email };
        return db.rawQuery("SELECT * FROM Reserve WHERE EMAIL=?", selectionArgs);
    }
    public Cursor getFavorites(String email){
        SQLiteDatabase db = this.getReadableDatabase();
        String[] selectionArgs = { email };
        return db.rawQuery("SELECT * FROM Favorite WHERE EMAIL=?", selectionArgs);
    }
    public Cursor getCar(String VIN){
        SQLiteDatabase db = this.getReadableDatabase();
        String[] selectionArgs = { VIN };
        return db.rawQuery("SELECT * FROM Car WHERE VIN=?", selectionArgs);
    }
    public Cursor getAllCars() {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {
                "FACTORY",
                "TYPE",
                "VIN",
                "MODEL",
                "PRICE",
                "IMG"
        };
        return db.query("Car", projection, null, null, null, null, null);
    }
    public int removeFavorite(String VIN, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        String tableName = "Favorite";
        String whereClause = "VIN = ? AND email = ?";
        String[] whereArgs = {VIN, email};
        try {
            int rowsDeleted = db.delete(tableName, whereClause, whereArgs);
            db.close();
            return rowsDeleted;
        } catch (Exception e) {
            return -1;
        }
    }
    public Cursor getLatestReservation(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM Reserve WHERE EMAIL = ? ORDER BY DATE_RESERVED,TIME_RESERVED DESC LIMIT 1",
                new String[]{email}
        );
        return cursor;
    }
    public Cursor getLatestFavorite(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM Favorite WHERE EMAIL = ? ORDER BY TIMED DESC LIMIT 1",
                new String[]{email}
        );
        return cursor;
    }
    public boolean isFavoriteExist(String email, String VIN) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Favorite WHERE EMAIL=? AND VIN=?", new String[]{email, VIN});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

}