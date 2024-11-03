package com.example.ek;

import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;
import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 12;
    public static final String DBname = "RealEstate.db";
    private Context context;

    public DBHelper(@Nullable Context context) {
        super(context, DBname, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS users (email TEXT PRIMARY KEY, password TEXT, firstName TEXT, lastName TEXT, role TEXT)");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS clients (email TEXT PRIMARY KEY, firstName TEXT, lastName TEXT, phoneNumber TEXT)");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS agents (email TEXT PRIMARY KEY, firstName TEXT, lastName TEXT, phoneNumber TEXT, about TEXT, province TEXT, city TEXT)");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS listings (listing_id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, description TEXT, price TEXT, province TEXT, city TEXT, listing_intent TEXT, listing_type TEXT, bedrooms TEXT, bathrooms TEXT, floors TEXT, area_size TEXT, agent_email TEXT, agent_cell TEXT, timestamp DATETIME DEFAULT CURRENT_TIMESTAMP)");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS listing_images (image_id INTEGER PRIMARY KEY AUTOINCREMENT, listing_id INTEGER, image_path TEXT, FOREIGN KEY (listing_id) REFERENCES listings(listing_id))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        //
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

    // Insert client information
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

    // Update client information
    public boolean updateClientData(String email, String firstName, String lastName, String phoneNumber) {
        SQLiteDatabase myDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("firstName", firstName);
        contentValues.put("lastName", lastName);
        contentValues.put("phoneNumber", phoneNumber);

        // Update the row where email matches
        int result = myDB.update("clients", contentValues, "email = ?", new String[]{email});
        return result > 0; // Returns true if the update was successful
    }

    // Retrieve client information
    public Cursor getClientData(String email) {
        SQLiteDatabase myDB = this.getReadableDatabase();
        return myDB.rawQuery("SELECT * FROM clients WHERE email = ?", new String[]{email});
    }

    // Insert agent information
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

    // Update agent information
    public boolean updateAgentData(String email, String firstName, String lastName, String phoneNumber, String about, String province, String city) {
        SQLiteDatabase myDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("firstName", firstName);
        contentValues.put("lastName", lastName);
        contentValues.put("phoneNumber", phoneNumber);
        contentValues.put("about", about);
        contentValues.put("province", province);
        contentValues.put("city", city);

        // Update the row where email matches
        int result = myDB.update("agents", contentValues, "email = ?", new String[]{email});
        return result > 0; // Returns true if the update was successful
    }

    // Retrieve agent information
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

    // Insert new listing
    public boolean insertListingData(String title, String description, String price, String province, String city,
                                     String listingIntent, String listingType, String bedrooms, String bathrooms,
                                     String floors, String areaSize, String agentEmail, String agentCell, ArrayList<Bitmap> images) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", title);
        contentValues.put("description", description);
        contentValues.put("price", price);
        contentValues.put("province", province);
        contentValues.put("city", city);
        contentValues.put("listing_intent", listingIntent);
        contentValues.put("listing_type", listingType);
        contentValues.put("bedrooms", bedrooms);
        contentValues.put("bathrooms", bathrooms);
        contentValues.put("floors", floors);
        contentValues.put("area_size", areaSize);
        contentValues.put("agent_email", agentEmail);
        contentValues.put("agent_cell", agentCell);

        long listingId = db.insert("listings", null, contentValues);

        if (listingId != -1) {
            for (Bitmap image : images) {
                String imagePath = saveImageToInternalStorage(image);
                ContentValues imageValues = new ContentValues();
                imageValues.put("listing_id", listingId);
                imageValues.put("image_path", imagePath);
                db.insert("listing_images", null, imageValues);
            }
            return true;
        } else {
            return false;
        }
    }

    private String saveImageToInternalStorage(Bitmap bitmap) {
        ContextWrapper contextWrapper = new ContextWrapper(context);
        File directory = contextWrapper.getDir("imageDir", Context.MODE_PRIVATE);
        String imageName = "img_" + UUID.randomUUID().toString() + ".jpg";
        File imageFile = new File(directory, imageName);

        try (FileOutputStream fos = new FileOutputStream(imageFile)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return imageFile.getAbsolutePath();
    }

    // Retrieve listing information with the first image for each listing
    public Cursor getListingData(String agent_email) {
        SQLiteDatabase myDB = this.getReadableDatabase();
        return myDB.rawQuery(
                "SELECT l.*, li.image_path " +
                        "FROM listings l " +
                        "LEFT JOIN listing_images li ON l.listing_id = li.listing_id " +
                        "WHERE l.agent_email = ? " +
                        "GROUP BY l.listing_id",
                new String[]{agent_email}
        );
    }

}
