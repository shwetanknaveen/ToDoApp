package com.example.todo3.ui.helper;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.todo3.R;
import com.example.todo3.pojo.ToDoTask;
import com.example.todo3.ui.fragment.AlarmManagement;
import com.example.todo3.ui.fragment.EditTaskFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DetailTaskDialogFragment extends DialogFragment {
    private TextView mTextViewTaskTitle_detail;
    private TextView mTextViewTaskDesc_detail;
    private TextView mTextViewTaskDate_detail;
    private TextView mTextViewTaskPriority_detail;
    private ImageButton mBtnDeleteTask;
    private ImageButton mBtnEditTask;
    private Button mBtnAlarmManage;
    private ToDoTask mToDoTask;
    private DatabaseReference mRef;
    private FirebaseDatabase mDatabase;
    private Context mContext;
    private View mView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public DetailTaskDialogFragment() {
    }

    public DetailTaskDialogFragment(ToDoTask mToDoTask,Context context,View view) {
        this.mToDoTask = mToDoTask;
        this.mDatabase = FirebaseDatabase.getInstance();
        this.mRef = mDatabase.getReference("Tasks");
        this.mContext = context;
        this.mView = view;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.task_detail_dialog_layout, container, false);

        mTextViewTaskTitle_detail = view.findViewById(R.id.textViewTaskTitle_detail);
        mTextViewTaskDesc_detail = view.findViewById(R.id.textViewTaskDesc_detail);
        mTextViewTaskDate_detail = view.findViewById(R.id.textViewTaskDate_detail);
        mTextViewTaskPriority_detail = view.findViewById(R.id.textViewTaskPriority_detail);

        mBtnDeleteTask = view.findViewById(R.id.btnDeleteTask);
        mBtnEditTask = view.findViewById(R.id.btnEditTask);
        mBtnAlarmManage = view.findViewById(R.id.btnAlarmManage);

        mTextViewTaskPriority_detail.setText(Integer.toString(mToDoTask.getPriority()));
        mTextViewTaskTitle_detail.setText(mToDoTask.getTitle());
        mTextViewTaskDesc_detail.setText(mToDoTask.getDescription());
        mTextViewTaskDate_detail.setText(mToDoTask.getDateAndTime().substring(0,10)+"\n"+mToDoTask.getDateAndTime().substring(11));
        //showing date and time on separate lines

        mBtnDeleteTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Task deletion....")
                        .setMessage("Do you want to delete this task ?");
                AlertDialog dialog = builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                }).setPositiveButton("Yes!", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        mRef.child(Integer.toString(mToDoTask.getId())).removeValue();
                        dismiss();
                    }
                }).create();

                dialog.show();
            }
        });
        mBtnEditTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new EditTaskFragment(mContext, mToDoTask);
                AppCompatActivity activity = (AppCompatActivity) mView.getContext();
                FragmentManager fragmentManager = activity.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentPlace, fragment);
                fragmentTransaction.addToBackStack(fragment.getClass().getName());//so that back button get backs to previous state
                fragmentTransaction.commit();
                dismiss();
            }
        });

        mBtnAlarmManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new AlarmManagement(mToDoTask,mContext);
                AppCompatActivity activity = (AppCompatActivity) mView.getContext();
                FragmentManager fragmentManager = activity.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentPlace, fragment);
                fragmentTransaction.addToBackStack(fragment.getClass().getName());//so that back button get backs to previous state
                fragmentTransaction.commit();
                dismiss();
            }
        });
        return view;
    }
}
