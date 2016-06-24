package com.aparnasridhar.elate.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.aparnasridhar.elate.data.MemoryProvider.Tables;

public class MemoryDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "elate.db";
    private static final int DATABASE_VERSION = 1;

    public MemoryDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + Tables.ITEMS + " ("
                + MemoryContract.ItemsColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + MemoryContract.ItemsColumns.SERVER_ID + " TEXT,"
                + MemoryContract.ItemsColumns.TITLE + " TEXT NOT NULL,"
                + MemoryContract.ItemsColumns.AUTHOR + " TEXT NOT NULL,"
                + MemoryContract.ItemsColumns.BODY + " TEXT NOT NULL,"
                + MemoryContract.ItemsColumns.THUMB_URL + " TEXT NOT NULL,"
                + MemoryContract.ItemsColumns.PHOTO_URL + " TEXT NOT NULL,"
                + MemoryContract.ItemsColumns.ASPECT_RATIO + " REAL NOT NULL DEFAULT 1.5,"
                + MemoryContract.ItemsColumns.PUBLISHED_DATE + " INTEGER NOT NULL DEFAULT 0,"
                + MemoryContract.ItemsColumns.TYPE + " TEXT"
                + ")" );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Tables.ITEMS);
        onCreate(db);
    }
}
