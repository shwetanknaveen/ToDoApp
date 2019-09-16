package com.example.todo3.adapter;


import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo3.R;
import com.example.todo3.pojo.ToDoTask;
import com.example.todo3.ui.fragment.EditTaskFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<ToDoTask> mTasks;
    private Context mContext;
    private DatabaseReference mRef;

    public TaskAdapter(List<ToDoTask> tasks, Context context) {
        this.mTasks = tasks;
        mContext = context;

        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference("Tasks");
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.task_item_layout, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TaskViewHolder holder, final int position) {

        final ToDoTask toDoTask = mTasks.get(position);

        holder.textViewTaskTitle.setText(toDoTask.getTitle());
        holder.textViewTaskDesc.setText(toDoTask.getDescription());

        holder.textViewTaskTag.setText(toDoTask.getTag().getTagName());

        GradientDrawable mGd = (GradientDrawable) holder.textViewTaskTag.getBackground().getCurrent();

        if (!TextUtils.isEmpty(toDoTask.getTag().getColor()))
            mGd.setColor(Color.parseColor(toDoTask.getTag().getColor()));
        else
            mGd.setColor(Color.parseColor("#123455"));//setting default color


        if (mTasks.get(position).getStatus()) {
            holder.checkBoxTaskStatus.setChecked(true);
        } else {
            holder.checkBoxTaskStatus.setChecked(false);
        }
        holder.textViewTaskPriority.setText(Integer.toString(toDoTask.getPriority()));
        holder.textViewTaskDate.setText(toDoTask.getDateAndTime());

        /*FOR MARKING TASKS COMPLETED OR UNCOMPLETED*/
        holder.checkBoxTaskStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.checkBoxTaskStatus.isChecked()) {
                    Toast.makeText(mContext, toDoTask.getTitle() + " marked COMPLETED", Toast.LENGTH_SHORT).show();
                    holder.checkBoxTaskStatus.setChecked(true);

                    toDoTask.setStatus(true);
                    mTasks.set(position, toDoTask);
                    mRef.child(Integer.toString(mTasks.get(position).getId())).setValue(toDoTask);
                } else {
                    Toast.makeText(mContext, mTasks.get(position).getTitle() + " marked UNCOMPLETED", Toast.LENGTH_SHORT).show();
                    holder.checkBoxTaskStatus.setChecked(false);
                    toDoTask.setStatus(false);
                    mTasks.set(position, toDoTask);
                    mRef.child(Integer.toString(mTasks.get(position).getId())).setValue(toDoTask);
                }

            }
        });
        /*CHANGING STATUS OF TASK LOGIC ENDS HERE*/

        /*DELETION OF TASK*/
        holder.eventLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Task deletion....")
                        .setMessage("Do you want to delete this task ?");
                AlertDialog dialog = builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        /* User clicked cancel so do some stuff */
                    }
                }).setPositiveButton("Yes!", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        mRef.child(Integer.toString(mTasks.get(position).getId())).removeValue();
                    }
                }).create();

                dialog.show();
            }
        });
        /*DELETION OF TASK ENDS HERE*/

        /*TASK EDITION*/
        holder.imageBtnEditTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new EditTaskFragment(mContext, mTasks.get(position));
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                FragmentManager fragmentManager = activity.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentPlace, fragment);
                fragmentTransaction.addToBackStack(fragment.getClass().getName());//so that back button get backs to previous state
                fragmentTransaction.commit();
            }
        });
        /*TASK EDITION ENDS HERE*/


    }

    @Override
    public int getItemCount() {
        return mTasks.size();
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBoxTaskStatus;
        TextView textViewTaskPriority;
        TextView textViewTaskTitle;
        TextView textViewTaskDesc;
        TextView textViewTaskDate;
        TextView textViewTaskTag;
        LinearLayout eventLinearLayout;
        ImageButton imageBtnEditTask;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBoxTaskStatus = itemView.findViewById(R.id.checkboxTaskStatus);
            textViewTaskPriority = itemView.findViewById(R.id.textViewTaskPriority);
            textViewTaskTitle = itemView.findViewById(R.id.textViewTaskTitle);
            textViewTaskDesc = itemView.findViewById(R.id.textViewTaskDesc);
            textViewTaskDate = itemView.findViewById(R.id.textViewTaskDate);
            textViewTaskTag = itemView.findViewById(R.id.textViewTaskTag);
            eventLinearLayout = itemView.findViewById(R.id.eventLinearLayout);
            imageBtnEditTask = itemView.findViewById(R.id.imgBtnEditTask);
        }
    }
}
