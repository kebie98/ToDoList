package com.example.todolist.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.todolist.data.TaskContract.TaskEntry;


public class TaskDbHelper extends SQLiteOpenHelper {

    //The name of the database
    private static final String DATABASE_NAME = "tasksDb.db ";

    //If you change the database schema you must increment the database version
    private static final int VERSION = 1;

    TaskDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    //Called when the tasks database is created for the first time.
    @Override
    public void onCreate(SQLiteDatabase db) {

        final String CREATE_TABLE = "CREATE TABLE " + TaskEntry.TABLE_NAME + " (" +
                TaskEntry._ID + "INTEGER PRIMARY KEY, " + TaskEntry.COLUMN_DESCRIPTION +
                " TEXT NOT NULL, " + TaskEntry.COLUMN_PRIORITY + " INTEGER NOT NULL);";

        db.execSQL(CREATE_TABLE);
    }

//This method discards the old table of data and calls onCreate to recreate a new one.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TaskEntry.TABLE_NAME);
        onCreate(db);
    }

}
