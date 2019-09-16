package com.example.todo3.ui.fragment;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.todo3.R;
import com.example.todo3.adapter.TagAdapter;
import com.example.todo3.pojo.ToDoTag;
import com.example.todo3.pojo.ToDoTask;
import com.example.todo3.ui.helper.DatePickerFragment;
import com.example.todo3.utilities.Utility;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Set;


public class AddTaskFragment extends Fragment {

    private ToDoTask mTaskToAdd = new ToDoTask();
    private String mDate;
    private Context mContext;
    private List<ToDoTag> mShowTagList = new ArrayList<>(); //this list stores all stored tags on firebase to give user option to add tag

    private EditText mEditTextPriority;
    private EditText mEditTextTitle;
    private EditText mEditTextDesc;
    private Button mBtnDeadline;
    private Button mAddTaskBtn;
    private RecyclerView mtagList;

    public AddTaskFragment(Context context) {
        mContext = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_task_fragment, container, false);
        final ShimmerFrameLayout shimmerFrameLayout = view.findViewById(R.id.shimmerForTag);
        shimmerFrameLayout.setAutoStart(true);

        mEditTextPriority = view.findViewById(R.id.editTextPriority);
        mEditTextTitle = view.findViewById(R.id.editTextTitle);
        mEditTextDesc = view.findViewById(R.id.editTextDesc);
        mBtnDeadline = view.findViewById(R.id.btnDate);
        mAddTaskBtn = view.findViewById(R.id.btnAddTask);

        mtagList = view.findViewById(R.id.tagList);
        mtagList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        //to show the tags in horizontal layout

        FirebaseDatabase databaseForTags = FirebaseDatabase.getInstance();
        DatabaseReference myRefForTags = databaseForTags.getReference("Tags");
        myRefForTags.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String, ToDoTag> map = (HashMap<String, ToDoTag>) dataSnapshot.getValue();
                Set<String> arrayList = map.keySet();
                mShowTagList.clear();
                for (String s : arrayList) {
                    mShowTagList.add(dataSnapshot.child(s).getValue(ToDoTag.class));
                }
                mtagList.setAdapter(new TagAdapter(mShowTagList, getContext()));
                shimmerFrameLayout.stopShimmerAnimation();
                shimmerFrameLayout.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        mBtnDeadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
                showTimePicker();
            }
        });
        mAddTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!(TextUtils.isEmpty(mEditTextDesc.getText()) && TextUtils.isEmpty(mEditTextTitle.getText()))) {   //both task title and task description should not be empty

                    if (!TextUtils.isEmpty(mEditTextPriority.getText().toString()))
                        mTaskToAdd.setPriority(Integer.parseInt(mEditTextPriority.getText().toString()));
                    else
                        mTaskToAdd.setPriority(1);//default priority in case of no priority entered
                    mTaskToAdd.setTitle(mEditTextTitle.getText().toString());
                    mTaskToAdd.setDescription(mEditTextDesc.getText().toString());
                    mTaskToAdd.setDateAndTime(mDate);
                    if (Utility.toDoTag != null) {
                        mTaskToAdd.setTag(Utility.toDoTag);
                        Log.i("added tag is -> ", Utility.toDoTag.getTagName());
                        Utility.toDoTag = null; //after setting tag value from utility class, set back utility class variable value
                        //  to null.
                    } else {
                        mTaskToAdd.setTag(Utility.defaultTag);
                    }
                    mTaskToAdd.setId(mTaskToAdd.hashCode());


                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("Tasks");
                    myRef.child(Integer.toString(mTaskToAdd.getId())).setValue(mTaskToAdd);
                    Toast.makeText(getContext(), getContext().getString(R.string.msg_success_addtask), Toast.LENGTH_SHORT).show();
                    getActivity().onBackPressed();  //move back to home fragment while showing Toast of task addition
                } else {
                    Toast.makeText(getContext(), "Enter at least title or description", Toast.LENGTH_LONG).show();
                }

            }
        });
        return view;
    }

    public void showDatePicker() {
        DatePickerFragment date = new DatePickerFragment();
        /**
         * Set Up Current Date Into dialog
         */
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        /**
         * Set Call back to capture selected date
         */
        date.setCallBack(ondate);
        date.show(getFragmentManager(), "Date Picker");
    }//show date picker ends here

    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            if (dayOfMonth < 10)
                mDate = "0" + dayOfMonth + "/";
            else
                mDate = dayOfMonth + "/";

            if (monthOfYear < 10)
                mDate += "0" + monthOfYear + "/";
            else
                mDate += monthOfYear + "/";

            mDate += year + "  ";
            mBtnDeadline.setText(mDate);
        }
    };

    public void showTimePicker() {


        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                if (hour < 10)
                    mDate += "0" + hour + " : ";
                else
                    mDate += hour + " : ";
                if (minute < 10)
                    mDate += "0" + minute;
                else
                    mDate += "" + minute;
                Toast.makeText(getContext(), mDate, Toast.LENGTH_SHORT).show();
            }
        }, Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), true);
        timePickerDialog.show();


    }


}
