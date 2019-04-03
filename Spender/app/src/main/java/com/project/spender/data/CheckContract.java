package com.project.spender.data;

import android.provider.BaseColumns;

public class CheckContract  {
    private CheckContract() {}

    public static final class ItemEntry implements BaseColumns {
        public final static String TABLE_NAME = "Items";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_NAME = "name";
        public final static String COLUMN_PRICE = "price";
        public final static String COLUMN_SHOP = "shop";
        public final static String COLUMN_DATE = "date";
    }
}
