package com.example.ek;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 6;
    public static final String DBname = "RealEstate.db";

    public DBHelper(@Nullable Context context) {
        super(context, DBname, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS users (email TEXT PRIMARY KEY, password TEXT, firstName TEXT, lastName TEXT, role TEXT)");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS clients (email TEXT PRIMARY KEY, firstName TEXT, lastName TEXT, phoneNumber TEXT)");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS agents (email TEXT PRIMARY KEY, firstName TEXT, lastName TEXT, phoneNumber TEXT, about TEXT, province TEXT, city TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS messages (message_id INTEGER PRIMARY KEY AUTOINCREMENT, senderPhoneNumber TEXT, receiverPhoneNumber TEXT, message TEXT, status TEXT DEFAULT 'delivered', timestamp DATETIME DEFAULT CURRENT_TIMESTAMP)");
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

    // Insert or update client information
    public boolean insertAgentData(String email, String firstName, String lastName, String phoneNumber, String about, String province, String city) {
        SQLiteDatabase myDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", email);
        contentValues.put("firstName", firstName);
        contentValues.put("lastName", lastName);
        contentValues.put("phoneNumber", phoneNumber);
        contentValues.put("about", about);
        contentValues.put("province", province);
        contentValues.put("city", city);
        long result = myDB.insertWithOnConflict("agents", null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
        return result != -1;
    }

    // Retrieve client information
    public Cursor getAgentData(String email) {
        SQLiteDatabase myDB = this.getReadableDatabase();
        return myDB.rawQuery("SELECT * FROM agents WHERE email = ?", new String[]{email});
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

//    public Cursor getUserFullName(String email) {
//        SQLiteDatabase myDB = this.getReadableDatabase();
//        return myDB.rawQuery("SELECT firstName, lastName FROM users WHERE email = ?", new String[]{email});
//    }

    // Insert new message based on phone number
    public boolean insertMessage(String senderPhoneNumber, String receiverPhoneNumber, String message) {
        SQLiteDatabase myDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("senderPhoneNumber", senderPhoneNumber);
        contentValues.put("receiverPhoneNumber", receiverPhoneNumber);
        contentValues.put("message", message);
        contentValues.put("status", "delivered");  // Message status is "delivered" by default
        long result = myDB.insert("messages", null, contentValues);
        return result != -1;
    }

    // Retrieve messages between two phone numbers
    public boolean updateMessageStatusToRead(int messageId) {
        SQLiteDatabase myDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("status", "read");
        int rowsAffected = myDB.update("messages", contentValues, "message_id = ?", new String[]{String.valueOf(messageId)});
        return rowsAffected > 0;
    }
}
