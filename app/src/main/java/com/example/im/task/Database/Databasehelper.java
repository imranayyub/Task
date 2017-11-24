package com.example.im.task.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.im.task.Model.Contact;

/**
 * Created by Im on 02-11-2017.
 */

public class Databasehelper extends SQLiteOpenHelper {
    SQLiteDatabase db;
    Contact c = new Contact();
    //defining database variables.
    public static final String DATABASE_NAME = "Contact.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "Contact";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_APP = "app";
    public static final String COLUMN_PROFILEPIC = "profilepic";
    public static final String TABLE_CREATE = "create table Contact (NAME not null , EMAIL not null,APP not null,TIME not null,PROFILEPIC );";


    String DB_PATH = null;

    //constructor for Databasehelper class.
    public Databasehelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//executes the query stored in TABLLE_CREATE(Basically create a table.).
        db.execSQL(TABLE_CREATE);
        this.db = db;

    }

    //upgrades the table with newer version if there are  any changes in older version.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = "Drop if Exist " + TABLE_NAME;
        db.execSQL(query);
        onCreate(db);
    }

    //Inserts data into the table.
    public void insert(Contact c) {
        try {
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_EMAIL, c.getEmail());
            values.put(COLUMN_NAME, c.getName());
            values.put(COLUMN_APP, c.getApp());
            values.put(COLUMN_TIME, c.getDate());
            values.put(COLUMN_PROFILEPIC, c.getPic());
            db.insert(TABLE_NAME, null, values);
        } catch (Exception e) {
            e.printStackTrace();   //Very useful tool for diagnosing an Exception. It tells what happened and where in the code this happened.
        }
    }

    // Deletes all the data present currently in database.
    public void delete() {
        String query = "Delete from " + TABLE_NAME;
        db.execSQL(query);
    }

    //Checks if there is any record in DataBase.
    public Contact checkLogin() {
        db = this.getReadableDatabase(); //Gets the Readable database.
        String Query = "Select * from " + TABLE_NAME;
        Cursor cursor = db.rawQuery(Query, null);  //Cursor is the Interface which represents a 2 dimensional table of any database. When you try to retrieve some data using SELECT statement, then the database will first create a CURSOR object and return its reference to you.
        cursor.moveToFirst();   //Makes Cursor point to First Record.
        int count = cursor.getCount();
        String login_app, name, profilePic, email;

        if (count > 0) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                login_app = cursor.getString(cursor.getColumnIndex("APP"));       //Fetches data Stored in APP Column of the Table.
                c.setApp(login_app);
                name = cursor.getString(cursor.getColumnIndex("NAME"));
                c.setName(name);
                email = cursor.getString(cursor.getColumnIndex("EMAIL"));
                c.setEmail(email);
                profilePic = cursor.getString(cursor.getColumnIndex("PROFILEPIC"));
                c.setPic(profilePic);


            }
        }
        if (count == 0) {
            c.setApp("null");
        }
        return c;
    }


}