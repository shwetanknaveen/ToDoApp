package com.example.todo3.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.todo3.R;
import com.example.todo3.adapter.TaskAdapter;
import com.example.todo3.pojo.ToDoTask;
import com.example.todo3.utilities.Utility;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Set;


public class HomeFragment extends Fragment {
    private List<ToDoTask> mShowTasksList = new ArrayList<>();

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_home_fragment, container, false);

        final RecyclerView taskList = view.findViewById(R.id.homeScreenTaskList);
        taskList.setLayoutManager(new LinearLayoutManager(getContext()));


        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Tasks");

        final ShimmerFrameLayout shimmerFrameLayout = view.findViewById(R.id.shimmer);
        shimmerFrameLayout.setAutoStart(true);


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String, ToDoTask> map = (HashMap<String, ToDoTask>) dataSnapshot.getValue();
                Set<String> arrayList = map.keySet();   // verified with Toast that arrayList

                mShowTasksList.clear();     //to ensure that previously loaded tasks in recycler view don't get repeated

                for (String s : arrayList) {
                    mShowTasksList.add(dataSnapshot.child(s).getValue(ToDoTask.class));
                    //verified with toast that this list get all tasks from firebase

                }
                if(Utility.dateSortFlag)    //if sort by date switch is on, then sort the task list by date using comparator
                    Collections.sort(mShowTasksList,taskDateComparator);
                if(Utility.tagSortFlag)     //if sort by tag switch is on, then sort the task list by tag name using comparator
                    Collections.sort(mShowTasksList,tagTitleComparator);

                taskList.setAdapter(new TaskAdapter(mShowTasksList, getContext()));
                shimmerFrameLayout.stopShimmerAnimation();
                shimmerFrameLayout.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        }); //addValueEventListner() ends here


        return view;
    }//HomeFragment class onCreateView() ends here

    Comparator<ToDoTask> taskDateComparator = new Comparator<ToDoTask>() {
        @Override
        public int compare(ToDoTask t1, ToDoTask t2) {
            //coming date format is DD/MM/YYYY<2 SPACES>HH<SPACE>:<SPACE>MM
            //format in which date should be compared is YYYYMMDDHH<SPACE>:<SPACE>MM
            String time1 = t1.getDateAndTime();
            String newDateT1 = time1.substring(6,9)+time1.substring(3,4)+time1.substring(0,1)+time1.substring(12);

            String time2 = t2.getDateAndTime();
            String newDateT2 = time2.substring(6,9)+time2.substring(3,4)+time2.substring(0,1)+time2.substring(12);

            //return t1.getDateAndTime().compareTo(t2.getDateAndTime());
            return newDateT1.compareTo(newDateT2);
        }
    };
    Comparator<ToDoTask> tagTitleComparator = new Comparator<ToDoTask>() {
        @Override
        public int compare(ToDoTask t1, ToDoTask t2) {
            return t1.getTag().getTagName().compareTo(t2.getTag().getTagName());
        }
    };

}
