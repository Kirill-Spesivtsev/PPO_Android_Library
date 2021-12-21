package com.example.lab_2_library;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "AndroidLibrary.db";
    private static final int SCHEMA = 1;
    static final String TABLE = "books";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_AUTHOR = "author";
    public static final String COLUMN_YEAR = "year";
    public static final String COLUMN_PUBLISHER = "publisher";
    public static final String COLUMN_PAGECOUNT = "pagecount";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE books (" + ""
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TITLE + " TEXT, "
                + COLUMN_AUTHOR + " TEXT, "
                + COLUMN_YEAR + " INTEGER, "
                + COLUMN_PUBLISHER + " TEXT, "
                + COLUMN_PAGECOUNT + " INTEGER);");

        //db.execSQL("INSERT INTO "+ TABLE +" (" + COLUMN_TITLE+ ", " + COLUMN_YEAR  + ") VALUES ('Том Смит', 1981);");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,  int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE);
        onCreate(db);
    }

}
