package com.assignment.call.SQLiteDB;

import android.provider.BaseColumns;

public class DatabaseConstants {
    private DatabaseConstants() {
    }

    public static abstract class CallDatabaseEntry implements BaseColumns {
        // Table ProductCategory
        public static final String TABLE_NAME_CALL_ENTRY = "call_entry";
        //Column names
        public static final String COLUMN_CALL_ID = "callId";//pk
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_PHONE_NUMBER = "phoneNumber";
        public static final String COLUMN_TIME_STAMP = "timeStamp";
        public static final String COLUMN_QUERY = "query";


    }
}
