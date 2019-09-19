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


public class EditTaskFragment extends Fragment {

    private Context mContext;
    private ToDoTask mEditedTask;
    private ToDoTask mOldTask;
    private List<ToDoTag> mShowTagList = new ArrayList<>(); //this list stores all stored tags on firebase to give user option to add tag
    private RecyclerView mtagList;
    private Button mBtnNewDeadline;
    private Button mBtnEditTask;
    private String mDate;
    private EditText mEditTextPriority;
    private EditText mEditTextTitle;
    private EditText mEditTextDesc;
    private boolean mNewDataFlag;


    public EditTaskFragment() {
        // Required empty public constructor
    }

    public EditTaskFragment(Context context,ToDoTask oldTask) {
        mContext = context;
        mEditedTask = new ToDoTask();
        mEditedTask.setId(oldTask.getId());
        mOldTask = oldTask;
        mNewDataFlag = false;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_task, container, false);
        mtagList = view.findViewById(R.id.tagList_editTask);
        mtagList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        //to show the tags in horizontal layout
        mBtnNewDeadline = view.findViewById(R.id.btnDate_editTask);
        mBtnEditTask = view.findViewById(R.id.btnEditTask);

        mEditTextPriority = view.findViewById(R.id.editTextPriority_editTask);
        mEditTextPriority.setHint(Integer.toString(mOldTask.getPriority()));

        mEditTextTitle = view.findViewById(R.id.editTextTitle_editTask);
        mEditTextTitle.setHint(mOldTask.getTitle());

        mEditTextDesc = view.findViewById(R.id.editTextDesc_editTask);
        mEditTextDesc.setHint(mOldTask.getDescription());


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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mBtnNewDeadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
                showTimePicker();
            }
        });

        mBtnEditTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(mEditTextPriority.getText().toString())) {
                    mEditedTask.setPriority(Integer.parseInt(mEditTextPriority.getText().toString()));
                    mNewDataFlag = true;
                }
                    else
                    mEditedTask.setPriority(mOldTask.getPriority());//retaining old priority

                if(!TextUtils.isEmpty(mEditTextTitle.getText().toString())) {
                    mEditedTask.setTitle(mEditTextTitle.getText().toString());
                    mNewDataFlag = true;
                }
                    else
                    mEditedTask.setTitle(mOldTask.getTitle());//retaining old title

                if(!TextUtils.isEmpty(mEditTextDesc.getText().toString())) {
                    mEditedTask.setDescription(mEditTextDesc.getText().toString());
                    mNewDataFlag = true;
                }
                else
                    mEditedTask.setDescription(mOldTask.getDescription());//retaining old description

                if(!TextUtils.isEmpty(mDate)) {
                    mEditedTask.setDateAndTime(mDate);
                    mNewDataFlag = true;
                }
                    else
                    mEditedTask.setDateAndTime(mOldTask.getDateAndTime());

                if(Utility.toDoTag != null)
                {
                    mNewDataFlag = true;
                    mEditedTask.setTag(Utility.toDoTag);
                    Utility.toDoTag = null;
                }
                else
                    mEditedTask.setTag(mOldTask.getTag());//retaining old tag

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("Tasks");
                myRef.child(Integer.toString(mEditedTask.getId())).setValue(mEditedTask);

                if(mNewDataFlag)
                    Toast.makeText(getContext(), "Task Edited!", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getContext(), "Nothing new added!", Toast.LENGTH_LONG).show();
                getActivity().onBackPressed();

            }
        });
        return view;
    }


    public void showDatePicker() {
        DatePickerFragment date = new DatePickerFragment();

        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
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
            mBtnNewDeadline.setText(mDate);
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
                mBtnNewDeadline.setText(mDate);
            }
        }, Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), true);
        timePickerDialog.show();


    }


}
