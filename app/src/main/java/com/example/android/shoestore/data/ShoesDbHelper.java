package com.example.android.shoestore.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.shoestore.data.ShoesContract.ShoeEntry;

public final class ShoesDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = ShoesDbHelper.class.getSimpleName();
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "shoesStore";

    public ShoesDbHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_SHOES_TABLE = "CREATE TABLE " + ShoeEntry.TABLE_NAME + " (" +
                ShoeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ShoeEntry.COLUMN_TYPE + " TEXT NOT NULL, " +
                ShoeEntry.COLUMN_COLOUR + " TEXT, " +
                ShoeEntry.COLUMN_GENDER + " INTEGER NOT NULL, " +
                ShoeEntry.COLUMN_SIZE + " INTEGER NOT NULL DEFAULT 0);";
        db.execSQL(SQL_CREATE_SHOES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
