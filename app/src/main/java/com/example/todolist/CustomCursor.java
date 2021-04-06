package com.example.todolist;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.R;
import com.example.todolist.data.TaskContract;

public class CustomCursor extends RecyclerView.Adapter<CustomCursor.TaskViewHolder>{

    //Class variables
    private Cursor mCursor;
    private Context mContext;


    /**
     * Constructor for the CustomCursorAdapter that initializes the Context.
     *
     * @param mContext the current Context
     */

    public CustomCursor(Context mContext) {
        this.mContext = mContext;
    }

    //Called when ViewHolders are created to fill a RecyclerView.
    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.task_layout, parent, false);

        return new TaskViewHolder(view);
    }

    //Called by the RecyclerView to display data at a specified position in the Cursor.
    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {

        int idIndex = mCursor.getColumnIndex(TaskContract.TaskEntry._ID);
        int descriptionIndex = mCursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_DESCRIPTION);
        int priorityIndex = mCursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_PRIORITY);

        mCursor.moveToPosition(position);

        //Determines the values of the wanted data
        final int id = mCursor.getInt(idIndex);
        String description = mCursor.getString(descriptionIndex);
        int priority = mCursor.getInt(priorityIndex);

        //Set values
        holder.itemView.setTag(id);
        holder.taskDescriptionView.setText(description);

        String priorityString = "" + priority;
        holder.priorityView.setText(priorityString);

        GradientDrawable priorityCircle = (GradientDrawable) holder.priorityView.getBackground();

        //Get the appropriate background color based on the priority.
        int priorityColor = getPriorityColor(priority);
        priorityCircle.setColor(priorityColor);

    }
    /*
    Helper method for selecting the correct priority circle color.
    P1 = red, P2 = orange, P3 = yellow
     */
    private int getPriorityColor(int priority) {
        int priorityColor = 0;

        switch (priority){
            case 1: priorityColor = ContextCompat.getColor(mContext, R.color.materialRed);
            break;
            case 2: priorityColor = ContextCompat.getColor(mContext, R.color.materialOrange);
            break;
            case 3: priorityColor = ContextCompat.getColor(mContext, R.color.materialYellow);
            break;
            default: break;
        }
        return priorityColor;

    }

    /*
    Returns the number of items to display.
     */
    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
    }

    public Cursor swapCursor(Cursor c) {
        if (mCursor == c) {
            return null;
        }
        Cursor temp = mCursor;
        this.mCursor = c;

        //Checks if this is a valid cursor, then update the cursor.
        if (c != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }
    //Inner class for creating ViewHolders
    class TaskViewHolder extends RecyclerView.ViewHolder {

        TextView taskDescriptionView;
        TextView priorityView;


        public TaskViewHolder(View itemView) {
            super(itemView);

            taskDescriptionView = (TextView) itemView.findViewById(R.id.taskDescription);
            priorityView = (TextView) itemView.findViewById(R.id.priorityTextView);
        }
    }
}
