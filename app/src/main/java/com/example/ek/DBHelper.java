package com.example.ek;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 4;
    public static final String DBname = "RealEstate.db";

    public DBHelper(@Nullable Context context) {
        super(context, DBname, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE users (email TEXT PRIMARY KEY, password TEXT, firstName TEXT, lastName TEXT, role TEXT)");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS clients (email TEXT PRIMARY KEY, firstName TEXT, lastName TEXT, phoneNumber TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
//        if (oldVersion < 4) {
//
//        }
    }

    public boolean insertData(String email, String password, String firstName, String lastName, String role){
        SQLiteDatabase myDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", email);
        contentValues.put("password", password);
        contentValues.put("firstName", firstName);
        contentValues.put("lastName", lastName);
        contentValues.put("role", role);
        long result = myDB.insert("users", null, contentValues);
        if (result == -1) return false;
        else return true;
    }

    // Insert or update client information
    public boolean insertClientData(String email, String firstName, String lastName, String phoneNumber) {
        SQLiteDatabase myDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", email);
        contentValues.put("firstName", firstName);
        contentValues.put("lastName", lastName);
        contentValues.put("phoneNumber", phoneNumber);
        long result = myDB.insertWithOnConflict("clients", null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
        return result != -1;
    }

    // Retrieve client information
    public Cursor getClientData(String email) {
        SQLiteDatabase myDB = this.getReadableDatabase();
        return myDB.rawQuery("SELECT * FROM clients WHERE email = ?", new String[]{email});
    }

    public boolean checkUsername(String email){
        SQLiteDatabase myDB = this.getWritableDatabase();
        Cursor cursor = myDB.rawQuery("select * from users where email = ?", new String[]{email});
        if (cursor.getCount() > 0)
            return true;
        else return false;
    }

    public boolean checkUser(String email, String password){
        SQLiteDatabase myDB = this.getWritableDatabase();
        Cursor cursor = myDB.rawQuery("select * from users where email = ? and password = ?", new String[]{email, password});
        if (cursor.getCount() > 0)
            return true;
        else return false;
    }

    public Cursor getUserFullName(String email) {
        SQLiteDatabase myDB = this.getReadableDatabase();
        return myDB.rawQuery("SELECT firstName, lastName FROM users WHERE email = ?", new String[]{email});
    }
}
