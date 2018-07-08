package com.example.android.shoestore.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.example.android.shoestore.data.ShoesContract.ShoeEntry;

public class ShoesProvider extends ContentProvider {

    private static final String LOG_TAG = ShoesProvider.class.getSimpleName();
    private static final int SHOES = 100;
    private static final int SHOES_ID = 101;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(ShoesContract.CONTENT_AUTHORITY, ShoesContract.PATH_SHOES, SHOES);
        sUriMatcher.addURI(ShoesContract.CONTENT_AUTHORITY, ShoesContract.PATH_SHOES + "/#", SHOES_ID);
    }

    private ShoesDbHelper dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = new ShoesDbHelper(getContext());
        return false;
    }

    @Override
    public Cursor query( Uri uri,  String[] projection,  String selection,  String[] selectionArgs,  String sortOrder) {

        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor;

        int match = sUriMatcher.match(uri);
        switch (match) {
            case SHOES:

                cursor = database.query(ShoeEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case SHOES_ID:
                selection = ShoeEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                cursor = database.query(ShoeEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case SHOES:
                return ShoeEntry.CONTENT_LIST_TYPE;
            case SHOES_ID:
                return ShoeEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case SHOES:
                return insertShoes(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertShoes(Uri uri, ContentValues values) {

        String type = values.getAsString(ShoeEntry.COLUMN_TYPE);
        if (type == null) {
            throw new IllegalArgumentException("Shoes require a type description");
        }

        String colour = values.getAsString(ShoeEntry.COLUMN_COLOUR);
        if (colour == null) {
            throw new IllegalArgumentException("Shoes require a supplier's name");
        }

        Integer gender = values.getAsInteger(ShoeEntry.COLUMN_GENDER);
        if (gender == null || !ShoeEntry.isValidGender(gender)) {
            throw new IllegalArgumentException("Shoes require valid gender");
        }

        Integer size = values.getAsInteger(ShoeEntry.COLUMN_SIZE);
        if (size != null && size < 0) {
            throw new IllegalArgumentException("Shoes require valid price");
        }

        Integer quantity = values.getAsInteger(ShoeEntry.COLUMN_QUANTITY);
        if (quantity != null && quantity < 0) {
            throw new IllegalArgumentException("Shoes require valid quantity");
        }

        Integer number = values.getAsInteger(ShoeEntry.COLUMN_NUMBER);
        if (number != null && number < 0) {
            throw new IllegalArgumentException("Shoes require valid supplier's number");
        }

        SQLiteDatabase database = dbHelper.getWritableDatabase();
        long id = database.insert(ShoeEntry.TABLE_NAME, null, values);
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case SHOES:
                rowsDeleted = database.delete(ShoeEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case SHOES_ID:
                selection = ShoeEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(ShoeEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
        if(rowsDeleted != 0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case SHOES:
                return updateShoes(uri, contentValues, selection, selectionArgs);
            case SHOES_ID:
                selection = ShoeEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateShoes(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updateShoes(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        if (values.containsKey(ShoeEntry.COLUMN_TYPE)) {
            String name = values.getAsString(ShoeEntry.COLUMN_TYPE);
            if (name == null) {
                throw new IllegalArgumentException("Shoes require a type description");
            }
        }

        if (values.containsKey(ShoeEntry.COLUMN_COLOUR)) {
            String name = values.getAsString(ShoeEntry.COLUMN_COLOUR);
            if (name == null) {
                throw new IllegalArgumentException("Shoes require a colour");
            }
        }

        if (values.containsKey(ShoeEntry.COLUMN_GENDER)) {
            Integer gender = values.getAsInteger(ShoeEntry.COLUMN_GENDER);
            if (gender == null || !ShoeEntry.isValidGender(gender)) {
                throw new IllegalArgumentException("Shoes require valid gender description");
            }
        }

        if (values.containsKey(ShoeEntry.COLUMN_SIZE)) {
            Integer size = values.getAsInteger(ShoeEntry.COLUMN_SIZE);
            if (size != null && size < 0) {
                throw new IllegalArgumentException("Shoes require valid size");
            }
        }

        if (values.containsKey(ShoeEntry.COLUMN_QUANTITY)) {
            Integer quantity = values.getAsInteger(ShoeEntry.COLUMN_QUANTITY);
            if (quantity != null && quantity < 0) {
                throw new IllegalArgumentException("Shoes require valid size");
            }
        }

        if (values.containsKey(ShoeEntry.COLUMN_NUMBER)) {
            Integer number = values.getAsInteger(ShoeEntry.COLUMN_NUMBER);
            if (number != null && number < 0) {
                throw new IllegalArgumentException("Shoes require valid size");
            }
        }

        if (values.size() == 0) {
            return 0;
        }

        SQLiteDatabase database = dbHelper.getWritableDatabase();

        int rowsUpdated = database.update(ShoeEntry.TABLE_NAME, values, selection, selectionArgs);
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}
