package com.example.todo3.ui.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.todo3.R;
import com.example.todo3.adapter.TaskAdapter;
import com.example.todo3.pojo.ToDoTask;
import com.example.todo3.ui.helper.DatePickerFragment;
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


public class ShowByDateFragment extends Fragment {
    private String mDate;
    private List<ToDoTask> mShowTasksList = new ArrayList<>();
    DatabaseReference myRef;
    FirebaseDatabase mDatabase;
    RecyclerView mTaskList;
    TextView mTextView;


    public ShowByDateFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_show_by_date_fragment, container, false);
        mTaskList = view.findViewById(R.id.byDateTaskList);
        mTaskList.setLayoutManager(new LinearLayoutManager(getContext()));
        mTextView = view.findViewById(R.id.textViewDateFrag);
        showDatePicker();
        mDatabase = FirebaseDatabase.getInstance();
        myRef = mDatabase.getReference("Tasks");

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
        /**
         * Set Call back to capture selected date
         */
        date.setCallBack(ondate);
        if(date.isCancelable())
        date.show(getFragmentManager(), "Date Picker");
    }//show date picker ends here

    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            monthOfYear += 1;
            if (dayOfMonth < 10)
                mDate = "0" + dayOfMonth + "/";
            else
                mDate = dayOfMonth + "/";

            if (monthOfYear < 10)
                mDate += "0" + monthOfYear + "/";
            else
                mDate += monthOfYear + "/";

            mDate += year + "  ";
            mTextView.setText("Tasks for date "+mDate);
            fetchData();
        }



    };

    public void fetchData()
    {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String, ToDoTask> map = (HashMap<String, ToDoTask>) dataSnapshot.getValue();
                Set<String> arrayList = map.keySet();

                mShowTasksList.clear();     //to ensure that previously loaded tasks in recycler view don't get repeated

                for(String s : arrayList)
                {
                    if(dataSnapshot.child(s).getValue(ToDoTask.class).getDateAndTime().contains(mDate.trim()))
                        mShowTasksList.add(dataSnapshot.child(s).getValue(ToDoTask.class));

                }
                mTaskList.setAdapter(new TaskAdapter(mShowTasksList,getContext()));
                if(mShowTasksList.size()==0)
                    mTextView.setText("Tasks for date " + mDate + "\n No task found!");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
