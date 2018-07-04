package com.example.android.shoestore.data;

import android.provider.BaseColumns;

public final class ShoesContract {

    private ShoesContract(){}

    public static abstract class ShoeEntry implements BaseColumns{

        public static final String TABLE_NAME = "shoes";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_COLOUR = "colour";
        public static final String COLUMN_GENDER = "gender";
        public static final String COLUMN_SIZE = "size";

        public static final int GENDER_TYPE_METRO = 0;
        public static final int GENDER_TYPE_MALE = 1;
        public static final int GENDER_TYPE_FEMALE = 2;
    }

}
