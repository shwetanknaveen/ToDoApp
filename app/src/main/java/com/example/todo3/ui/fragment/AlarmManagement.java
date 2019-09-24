package com.example.todo3.ui.fragment;


import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.todo3.R;
import com.example.todo3.pojo.ToDoTask;
import com.example.todo3.ui.helper.AlertReceiver;
import com.example.todo3.ui.helper.DatePickerFragment;

import java.util.Calendar;

public class AlarmManagement extends Fragment {


    private ToDoTask mToDoTask;
    private Context mContext;
    private int mYear, mDay, mMonth, mHour, mMinute;
    private TextView mTextViewAlarmDetail;

    public AlarmManagement()
    {

    }

    public AlarmManagement(ToDoTask toDoTask, Context context) {
        mToDoTask = toDoTask;
        mContext = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_alarm_management, container, false);
        Button btnAlarmTime = view.findViewById(R.id.btnAlarmTime);
        Button btnSetAlarm = view.findViewById(R.id.btnSetAlarm);
        mTextViewAlarmDetail = view.findViewById(R.id.textViewAlarmDetail);
        mTextViewAlarmDetail.setText("Set alarm for \""+mToDoTask.getTitle()+"\"\nDeadline is -:\n"+
                mToDoTask.getDateAndTime().substring(0,10)+"\n"+mToDoTask.getDateAndTime().substring(11));
        btnAlarmTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });

        btnSetAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                c.set(Calendar.YEAR, mYear);
                c.set(Calendar.MONTH, mMonth);
                c.set(Calendar.DAY_OF_MONTH, mDay);
                c.set(Calendar.HOUR_OF_DAY, mHour);
                c.set(Calendar.MINUTE, mMinute);
                c.set(Calendar.SECOND, 0);
                startAlarm(c);
            }
        });
        return view;
    }

    private void showDatePicker() {
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

            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            showTimePicker();
        }
    };

    private void showTimePicker() {

        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                mHour = hour;
                mMinute = minute;

                mTextViewAlarmDetail.setText("Alarm time selected for task \""+mToDoTask.getTitle()+"\" on time :\n"
                        +mDay+"/"+mMonth+"/"+mYear+" at\n"+mHour+" : "+mMinute);
            }
        }, Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), true);
        timePickerDialog.show();


    }

    private void startAlarm(Calendar c) {
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getContext(), AlertReceiver.class);
        intent.putExtra("taskTitle", mToDoTask.getTitle());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Log.i("In AlarmManagement",intent.getStringExtra("taskTitle"));
        if (c.before(Calendar.getInstance())) {
            Toast.makeText(mContext, "Selected time has passed!", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(mContext, "Alarm has been set!", Toast.LENGTH_LONG).show();
        alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);

        if (android.os.Build.VERSION.SDK_INT >= 19) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
        }

    }

    private void cancelAlarm() {
        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(mContext, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 1, intent, 0);

        alarmManager.cancel(pendingIntent);
        Toast.makeText(mContext, "Alarm cancelled", Toast.LENGTH_SHORT).show();
    }


}
