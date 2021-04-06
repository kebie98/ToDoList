package com.example.todolist;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.todolist.data.TaskContract;

public class AddTask extends AppCompatActivity {

    //Declare a member variable to keep track of a task's selected mPriority
    private int mPriority;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_task);

        //Initialize to highest mPriority by default (mPriority = 1)
        ((RadioButton) findViewById(R.id.radButton1)).setChecked(true);
        mPriority = 1;
    }

    //OnClickAddTask is called when the "ADD" button is clicked.

    public void onClickAddTask(View view) {
        //Checkif the EditText is empty, if not retrieve input and store it in a ContentValues object
        String input = ((EditText) findViewById(R.id.editTextTaskDescription)).getText().toString();

        if (input.length() == 0) {
            return;
        }

        //Insert new task data via a ContentResolver
        ContentValues contentValues = new ContentValues();

        contentValues.put(TaskContract.TaskEntry.COLUMN_DESCRIPTION, input);
        contentValues.put(TaskContract.TaskEntry.COLUMN_PRIORITY, mPriority);

        Uri uri = getContentResolver().insert(TaskContract.TaskEntry.CONTENT_URI, contentValues);


        //Finish activity (this returns back to MainActivity)
        finish();

    }
    /*
    onPrioritySelected is called whenever a priority button is clicked.
    It changes the value of mPriority based on the selected button.
     */
    public void onPrioritySelected(View view) {

        if (((RadioButton) findViewById(R.id.radButton1)).isChecked()) {
            mPriority = 1;
        }else if (((RadioButton) findViewById(R.id.radButton2)).isChecked()) {
            mPriority = 2;
        }else if (((RadioButton) findViewById(R.id.radButton3)).isChecked()) {
            mPriority = 3;
        }
    }
}
