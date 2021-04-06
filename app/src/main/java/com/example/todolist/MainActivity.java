package com.example.todolist;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.data.TaskContract;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {
    //Constants for logging
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int TASK_LOADER_ID = 0;

    //Member variables for the adapter and RecyclerView
    private CustomCursor mCursor;
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set the RecyclerView to its corresponding view
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewTasks);

        //Sets RecyclerView to be a linear layout.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Initialize the adapter and attach to the RecyclerView
        mCursor = new CustomCursor(this);
        mRecyclerView.setAdapter(mCursor);


        /*
        Add a touch helper to recognize if a user swipes to delete an item. ItemTouchHelper
        uses callbacks to signal that a user is performing one of the actions.
         */
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            //Called when a user is swiping left or right.
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {

                int id = (int) viewHolder.itemView.getTag();

                //Identifies the URI for the item. Retrieves the item to be deleted.
                String stringId = Integer.toString(id);
                Uri uri = TaskContract.TaskEntry.CONTENT_URI;
                uri = uri.buildUpon().appendPath(stringId).build();

                //Delete item.
                getContentResolver().delete(uri, null, null);

                //Restarts loader after item is deleted.
                LoaderManager.getInstance(MainActivity.this).restartLoader(TASK_LOADER_ID,null, MainActivity.this);

            }
        }).attachToRecyclerView(mRecyclerView);

        /*
        Floating button is attached to its corresponding view. Add onClickListener to create
        new AddTask when clicked
         */
        FloatingActionButton fabButton = (FloatingActionButton) findViewById(R.id.fab);

        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Create a new intent to start an AddTask.
                Intent addTaskIntent = new Intent(MainActivity.this, AddTask.class);
                startActivity(addTaskIntent);

            }
        });

        LoaderManager.getInstance(this).initLoader(TASK_LOADER_ID, null, this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        //re-queries for all tasks
        LoaderManager.getInstance(this).restartLoader(TASK_LOADER_ID, null, this);

    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, final Bundle loaderArgs){

        return new AsyncTaskLoader<Cursor>(this){

            //Initalize a Cursor that holds all the task data.
            Cursor mTaskData = null;

            //onStartLoading() is called when a loader first starts loading data
            @Override
            protected void onStartLoading(){
                if (mTaskData != null){
                    //Delivers previously loaded data
                    deliverResult(mTaskData);
                }else {
                    //Forces new load
                    forceLoad();
                }
            }
            //loadInBackground() performs asynchronous loading of data
            @Override
            public Cursor loadInBackground() {

                //Loads all task data in the background and sorts by priority.
                try {
                    return getContentResolver().query(TaskContract.TaskEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            TaskContract.TaskEntry.COLUMN_PRIORITY);
                }catch (Exception e) {
                    Log.e(TAG, "Failed to asynchronously load data.");
                    e.printStackTrace();
                    return null;
                }
            }
            //deliverResult sends the result of the load, a Cursor, to the registered listener
            public void deliverResult(Cursor data) {
                mTaskData = data;
                super.deliverResult(data);
            }
        };

    }


    /**
     * Called when a previously created loader has finished its load.
     *
     * @param loader The Loader that has finished.
     * @param data The data generated by the Loader.
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data){
        //Update the data
        mCursor.swapCursor(data);
    }


    /**
     * Called when a previously created loader is being reset, which
     * makes the data unavailable.
     * onLoaderReset removes any references this activity had to the loader's data.
     *
     * @param loader The Loader that is being reset.
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader){
        mCursor.swapCursor(null);
    }
}

