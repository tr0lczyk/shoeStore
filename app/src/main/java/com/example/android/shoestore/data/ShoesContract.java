package com.example.android.shoestore.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class ShoesContract {

    private ShoesContract(){}

    public static final String CONTENT_AUTHORITY = "com.example.android.shoestore";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_SHOES = "shoestore";

    public static abstract class ShoeEntry implements BaseColumns{
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_SHOES);
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SHOES;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SHOES;

        public static final String TABLE_NAME = "shoes";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_COLOUR = "colour";
        public static final String COLUMN_GENDER = "gender";
        public static final String COLUMN_SIZE = "size";
        public static final String COLUMN_QUANTITY = "quantity";
        public static final String COLUMN_NUMBER = "number";

        public static final int GENDER_TYPE_METRO = 0;
        public static final int GENDER_TYPE_MALE = 1;
        public static final int GENDER_TYPE_FEMALE = 2;


        public static boolean isValidGender(int gender) {
            if (gender == GENDER_TYPE_METRO|| gender == GENDER_TYPE_MALE || gender == GENDER_TYPE_FEMALE) {
                return true;
            }
            return false;
        }
    }
}
