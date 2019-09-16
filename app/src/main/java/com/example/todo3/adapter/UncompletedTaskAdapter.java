package com.example.todo3.adapter;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.todo3.R;
import com.example.todo3.pojo.ToDoTask;
import java.util.List;

public class UncompletedTaskAdapter extends RecyclerView.Adapter<UncompletedTaskAdapter.UncompletedTaskViewHolder> {

    private List<ToDoTask> tasks;
    public UncompletedTaskAdapter(List<ToDoTask> tasks)
    {
        this.tasks = tasks;
    }

    @NonNull
    @Override
    public UncompletedTaskAdapter.UncompletedTaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.task_item_layout,parent,false);
        return new UncompletedTaskAdapter.UncompletedTaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UncompletedTaskAdapter.UncompletedTaskViewHolder holder, int position) {
        if(!tasks.get(position).getStatus()) {//bind only those views who have uncompleted status
            holder.textViewTaskTitle.setText(tasks.get(position).getTitle());
            holder.textViewTaskDesc.setText(tasks.get(position).getDescription());
            holder.textViewTaskTag.setText(tasks.get(position).getTag().getTagName());
            GradientDrawable gd = (GradientDrawable) holder.textViewTaskTag.getBackground().getCurrent();
            gd.setColor(Color.parseColor(tasks.get(position).getTag().getColor()));

            holder.checkBoxTaskStatus.setChecked(false);

            holder.textViewTaskPriority.setText(Integer.toString(tasks.get(position).getPriority()));
            holder.textViewTaskDate.setText(DateUtils.formatDateTime(holder.textViewTaskDate.getContext(), System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NUMERIC_DATE | DateUtils.FORMAT_SHOW_YEAR));
        }

    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public class UncompletedTaskViewHolder extends RecyclerView.ViewHolder
    {
        CheckBox checkBoxTaskStatus;
        TextView textViewTaskPriority;
        TextView textViewTaskTitle;
        TextView textViewTaskDesc;
        TextView textViewTaskDate;
        TextView textViewTaskTag;

        public UncompletedTaskViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBoxTaskStatus = (CheckBox)itemView.findViewById(R.id.checkboxTaskStatus);
            textViewTaskPriority  = (TextView)itemView.findViewById(R.id.textViewTaskPriority);
            textViewTaskTitle = (TextView)itemView.findViewById(R.id.textViewTaskTitle);
            textViewTaskDesc = (TextView)itemView.findViewById(R.id.textViewTaskDesc);
            textViewTaskDate = (TextView)itemView.findViewById(R.id.textViewTaskDate);
            textViewTaskTag = (TextView)itemView.findViewById(R.id.textViewTaskTag);
        }
    }
}

