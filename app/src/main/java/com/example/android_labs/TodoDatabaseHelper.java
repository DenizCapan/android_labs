package com.example.android_labs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class TodoDatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "TodoDatabaseHelper"; // Debugging Tag
    private static final String DATABASE_NAME = "todo_list.db";
    private static final int DATABASE_VERSION = 2; // Increment version for upgrades

    // Table and Column Names
    public static final String TABLE_NAME = "tasks";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TASK = "task";
    public static final String COLUMN_URGENT = "urgent";

    // SQL Query to Create Table
    private static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TASK + " TEXT NOT NULL, " +
                    COLUMN_URGENT + " INTEGER NOT NULL)";

    public TodoDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    /**
     * ✅ Insert a task into the database
     */
    public long insertTask(String task, boolean isUrgent) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TASK, task);
        values.put(COLUMN_URGENT, isUrgent ? 1 : 0);

        long result = db.insert(TABLE_NAME, null, values);
        db.close(); // Prevent memory leaks

        if (result == -1) {
            Log.e(TAG, "Error inserting task: " + task);
        } else {
            Log.d(TAG, "Task inserted successfully: " + task + " (ID: " + result + ")");
        }
        return result;
    }

    /**
     * ✅ Retrieve all tasks from the database
     */
    public Cursor getAllTasks() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        // Debugging: Print cursor data when fetching tasks
        printCursor(cursor);
        return cursor;
    }

    /**
     * ✅ Delete a task by ID
     */
    public int deleteTask(long taskId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_NAME, COLUMN_ID + "=?", new String[]{String.valueOf(taskId)});
        db.close();

        if (rowsDeleted > 0) {
            Log.d(TAG, "Task deleted successfully: ID " + taskId);
        } else {
            Log.e(TAG, "Failed to delete task: ID " + taskId);
        }
        return rowsDeleted;
    }

    /**
     * ✅ Debugging method to print database contents
     */
    public void printCursor(Cursor cursor) {
        if (cursor == null) {
            Log.e(TAG, "Cursor is null, database read failed.");
            return;
        }

        Log.d(TAG, "Database Version: " + this.getWritableDatabase().getVersion());
        Log.d(TAG, "Column Count: " + cursor.getColumnCount());

        StringBuilder columnNames = new StringBuilder("Column Names: ");
        for (int i = 0; i < cursor.getColumnCount(); i++) {
            columnNames.append(cursor.getColumnName(i)).append(" | ");
        }
        Log.d(TAG, columnNames.toString());

        Log.d(TAG, "Number of Results: " + cursor.getCount());

        while (cursor.moveToNext()) {
            Log.d(TAG, "Row: " +

                    cursor.getLong(0) + " | " +
                    cursor.getString(1) + " | " +
                    cursor.getInt(2));
        }
    }
}
