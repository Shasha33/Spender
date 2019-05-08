package com.project.spender.data;

import android.provider.BaseColumns;

public final class CheckContract  {
    private CheckContract() {}

    public static final class ItemEntry implements BaseColumns {
        public final static String TABLE_NAME = "Items";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_NAME = "name";
        public final static String COLUMN_SUM = "sum";
        public final static String COLUMN_PRICE = "price";
        public final static String COLUMN_QUANTITY = "quantity";
        public final static String COLUMN_CHECK_ID = "check_id";
    }

    public static final class CheckEntry implements BaseColumns {
        public final static String TABLE_NAME = "Checks";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_NAME = "name";
        public final static String COLUMN_TOTAL_SUM = "total_sum";
        public final static String COLUMN_SHOP = "shop";
        public final static String COLUMN_DATE = "date";
    }
}
