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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;


public class ShowUncompletedTaskFragment extends Fragment {

    public ShowUncompletedTaskFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_show_uncompleted_task_fragment, container, false);

        final RecyclerView uncomTaskList = view.findViewById(R.id.uncomTaskList);
        uncomTaskList.setLayoutManager(new LinearLayoutManager(getContext()));

        final List<ToDoTask> toDoUncomTasks = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Tasks");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String, ToDoTask> map = (HashMap<String, ToDoTask>) dataSnapshot.getValue();
                Set<String> arrayList = map.keySet();   // verified with Toast that arrayList

                toDoUncomTasks.clear();     //to ensure that previously loaded tasks in recycler view don't get repeated

                for (String s : arrayList) {
                    if (!dataSnapshot.child(s).getValue(ToDoTask.class).getStatus())
                        toDoUncomTasks.add(dataSnapshot.child(s).getValue(ToDoTask.class));
                }
                uncomTaskList.setAdapter(new TaskAdapter(toDoUncomTasks, getContext()));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        }); //addValueEventListner() ends here

        return view;
    }

}
