package com.example.todo3.adapter;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.example.todo3.R;
import com.example.todo3.pojo.ToDoTask;
import com.example.todo3.ui.helper.DetailTaskDialogFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<ToDoTask> mTasks;
    private Context mContext;
    private DatabaseReference mRef;
    private ToDoTask mSelectedTask;

    public TaskAdapter(List<ToDoTask> tasks, Context context) {
        this.mTasks = tasks;
        mContext = context;

        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference("Tasks");
        mSelectedTask = new ToDoTask();
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

        /*Maximum 15 characters title or description can be shown on home screen*/
        int titleLength = toDoTask.getTitle().length();
        int desLength = toDoTask.getDescription().length();

        if(titleLength>15)
            holder.textViewTaskTitle.setText(toDoTask.getTitle().substring(0,14)+"...");
        else
            holder.textViewTaskTitle.setText(toDoTask.getTitle());

        if(desLength>15)
            holder.textViewTaskDesc.setText(toDoTask.getDescription().substring(0,14)+"...");
        else
            holder.textViewTaskDesc.setText(toDoTask.getDescription());
        /*Precise title and description logic ends here*/
        holder.textViewTaskTag.setText(toDoTask.getTag().getTagName());

        GradientDrawable mGd = (GradientDrawable) holder.textViewTaskTag.getBackground().getCurrent();

        if (!TextUtils.isEmpty(toDoTask.getTag().getColor()))
            mGd.setColor(Color.parseColor(toDoTask.getTag().getColor()));
        else
            mGd.setColor(Color.parseColor("#123456"));//setting default color

        mGd = (GradientDrawable) holder.imageViewTaskPriority.getBackground().getCurrent();
        if(toDoTask.getPriority()==1) {
            mGd.setColor(Color.parseColor("#4DAA57"));
        }
        if(toDoTask.getPriority()==2) {
            mGd.setColor(Color.parseColor("#29487D"));
        }
        if(toDoTask.getPriority()>=3) {
            mGd.setColor(Color.parseColor("#B23121"));
        }

        mGd = (GradientDrawable) holder.imageViewTaskStatus.getBackground().getCurrent();
        if (mTasks.get(position).getStatus()) {
            holder.checkBoxTaskStatus.setChecked(true);
            mGd.setColor(Color.parseColor("#9EC9FF"));
            holder.lottieAnimationView.setAnimation("completed.json");
            holder.lottieAnimationView.playAnimation();
            holder.lottieAnimationView.setRepeatCount(LottieDrawable.INFINITE);
        } else {
            holder.checkBoxTaskStatus.setChecked(false);
            mGd.setColor(Color.parseColor("#3360FF"));
            holder.lottieAnimationView.setAnimation("pending.json");
            holder.lottieAnimationView.playAnimation();
            holder.lottieAnimationView.setRepeatCount(LottieDrawable.INFINITE);
        }
        holder.textViewTaskDate.setText(toDoTask.getDateAndTime().substring(0,10)); //show only date in home fragment

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

        /*Opening dialog fragment for deletion or editing task*/
        holder.eventRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//eventLinearLayout is the task item's parent item in its layout
                DetailTaskDialogFragment detailTaskDialogFragment = new DetailTaskDialogFragment(mTasks.get(position),mContext,view);
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                FragmentManager fragmentManager = activity.getSupportFragmentManager();
                detailTaskDialogFragment.show(fragmentManager,"Detail");

            }
        });
        /*DELETION or editing ends here*/

    }

    @Override
    public int getItemCount() {
        return mTasks.size();
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBoxTaskStatus;
        TextView textViewTaskTitle;
        TextView textViewTaskDesc;
        TextView textViewTaskDate;
        TextView textViewTaskTag;
        RelativeLayout eventRelativeLayout;
        TextView imageViewTaskStatus;
        TextView imageViewTaskPriority;
        LottieAnimationView lottieAnimationView;

        public TaskViewHolder(@NonNull final View itemView) {
            super(itemView);
            checkBoxTaskStatus = itemView.findViewById(R.id.checkboxTaskStatus);
            textViewTaskTitle = itemView.findViewById(R.id.textViewTaskTitle);
            textViewTaskDesc = itemView.findViewById(R.id.textViewTaskDesc);
            textViewTaskDate = itemView.findViewById(R.id.textViewTaskDate);
            textViewTaskTag = itemView.findViewById(R.id.textViewTaskTag);
            eventRelativeLayout = itemView.findViewById(R.id.taskItem);
            imageViewTaskStatus = itemView.findViewById(R.id.imageViewTaskStatus);
            imageViewTaskPriority = itemView.findViewById(R.id.imageViewPriority);
            lottieAnimationView = itemView.findViewById(R.id.lottieAnimation);



//            checkBoxTaskStatus.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    mSelectedTask = mTasks.get(getAdapterPosition());
//                    if (checkBoxTaskStatus.isChecked()) {
//                        Toast.makeText(mContext, mSelectedTask.getTitle() + " marked COMPLETED", Toast.LENGTH_SHORT).show();
//                        mSelectedTask.setStatus(true);
//                        mTasks.set(getAdapterPosition(), mSelectedTask);    //list of task get updated with that changed task
//                        mRef.child(Integer.toString(mTasks.get(getAdapterPosition()).getId())).setValue(mSelectedTask);
//                    } else {
//                        Toast.makeText(mContext, mTasks.get(getAdapterPosition()).getTitle() + " marked UNCOMPLETED", Toast.LENGTH_SHORT).show();
//                        mSelectedTask.setStatus(false);
//                        mTasks.set(getAdapterPosition(), mSelectedTask);
//                        mRef.child(Integer.toString(mTasks.get(getAdapterPosition()).getId())).setValue(mSelectedTask);
//                    }
//                }
//            });

        }
    }
}
