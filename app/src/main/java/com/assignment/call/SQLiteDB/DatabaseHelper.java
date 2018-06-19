package com.assignment.call.SQLiteDB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "callassignment.db";
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ", ";
    private static final String BLOB_TYPE = " BLOB";

    // Create/Delete Table
    private static final String SQL_CREATE_CALL_ENTRY_TABLE =
            "CREATE TABLE " + DatabaseConstants.CallDatabaseEntry.TABLE_NAME_CALL_ENTRY + " (" +
                    DatabaseConstants.CallDatabaseEntry.COLUMN_CALL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    DatabaseConstants.CallDatabaseEntry.COLUMN_NAME + TEXT_TYPE + COMMA_SEP +
                    DatabaseConstants.CallDatabaseEntry.COLUMN_EMAIL + TEXT_TYPE + COMMA_SEP +
                    DatabaseConstants.CallDatabaseEntry.COLUMN_PHONE_NUMBER + TEXT_TYPE + COMMA_SEP +
                    DatabaseConstants.CallDatabaseEntry.COLUMN_QUERY + TEXT_TYPE + COMMA_SEP +
                    DatabaseConstants.CallDatabaseEntry.COLUMN_TIME_STAMP + TEXT_TYPE +
                    " )";

    private static final String SQL_DELETE_CALL_ENTRY_TABLE =
            "DROP TABLE IF EXISTS " + DatabaseConstants.CallDatabaseEntry.TABLE_NAME_CALL_ENTRY;


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_CALL_ENTRY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_CALL_ENTRY_TABLE);
        onCreate(db);

    }
}
