package com.assignment.call.SQLiteDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.assignment.call.model.CallEntry;

import java.util.ArrayList;
import java.util.List;


public class DatabaseManager {
    private static final String TAG = DatabaseManager.class.getSimpleName();
    // Database fields
    private Context context = null;
    private SQLiteDatabase database = null;
    private DatabaseHelper dbHelper = null;

    public DatabaseManager(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        if (dbHelper == null) {
            dbHelper = new DatabaseHelper(context);
        }
        if (database == null) {
            database = dbHelper.getWritableDatabase();
        }
    }

    public void close() {
        dbHelper.close();
        database = null;
        dbHelper = null;
    }

    //Inserting/Deleting/Updating/Fetching Data From UI to DB : Methods - start

    public synchronized long insertCallEntryDetails(CallEntry callEntry) {
        long result = 0;
        try {
            if (database == null) {
                open();
            }

            /*
             DatabaseConstants.CallDatabaseEntry.COLUMN_NAME + TEXT_TYPE + COMMA_SEP +
                    DatabaseConstants.CallDatabaseEntry.COLUMN_EMAIL + TEXT_TYPE + COMMA_SEP +
                    DatabaseConstants.CallDatabaseEntry.COLUMN_PHONE_NUMBER + TEXT_TYPE + COMMA_SEP +
                    DatabaseConstants.CallDatabaseEntry.COLUMN_QUERY + TEXT_TYPE + COMMA_SEP +
                    DatabaseConstants.CallDatabaseEntry.COLUMN_TIME_STAMP + TEXT_TYPE +
             */
            ContentValues values = new ContentValues();
            values.put(DatabaseConstants.CallDatabaseEntry.COLUMN_NAME, callEntry.getName());
            values.put(DatabaseConstants.CallDatabaseEntry.COLUMN_EMAIL, callEntry.getEmailId());
            values.put(DatabaseConstants.CallDatabaseEntry.COLUMN_PHONE_NUMBER, callEntry.getPhoneNumber());
            values.put(DatabaseConstants.CallDatabaseEntry.COLUMN_QUERY, callEntry.getQuery());
            values.put(DatabaseConstants.CallDatabaseEntry.COLUMN_TIME_STAMP, callEntry.getTimeStamp());


            result = database.insert(DatabaseConstants.CallDatabaseEntry.TABLE_NAME_CALL_ENTRY, null, values);
            close();
        } catch (Exception ex) {

        }
        return result;
    }

    public synchronized List<CallEntry> getAllCallEntries() {
        if (database == null) {
            open();
        }
        List<CallEntry> detailsList = new ArrayList<CallEntry>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + DatabaseConstants.CallDatabaseEntry.TABLE_NAME_CALL_ENTRY;
        Cursor cursor = database.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                /*
                   values.put(DatabaseConstants.CallDatabaseEntry.COLUMN_NAME, callEntry.getName());
            values.put(DatabaseConstants.CallDatabaseEntry.COLUMN_EMAIL, callEntry.getEmailId());
            values.put(DatabaseConstants.CallDatabaseEntry.COLUMN_PHONE_NUMBER, callEntry.getPhoneNumber());
            values.put(DatabaseConstants.CallDatabaseEntry.COLUMN_TIME_STAMP, callEntry.getTimeStamp());
            values.put(DatabaseConstants.CallDatabaseEntry.COLUMN_QUERY, callEntry.getQuery());
                 */
                CallEntry category = new CallEntry();
                category.setCallId(Integer.parseInt(cursor.getString(0)));
                category.setName(cursor.getString(1));
                category.setEmailId(cursor.getString(2));
                category.setPhoneNumber(cursor.getString(3));
                category.setQuery(cursor.getString(4));
                category.setTimeStamp(cursor.getString(5));

                detailsList.add(category);
            } while (cursor.moveToNext());
        }
        close();
        return detailsList;
    }

    public synchronized int deleteCallEntry(int callId) {
        int result = 0;
        if (database == null) {
            open();
        }
        String tableName = DatabaseConstants.CallDatabaseEntry.TABLE_NAME_CALL_ENTRY;
        String where = "callId='" + callId + "'";

        result = database.delete(tableName, where, null);
        Log.i(TAG, " deleteTableRow Deleted rows count: " + result);
        close(); // Closing database connection
        return result;
    }


}