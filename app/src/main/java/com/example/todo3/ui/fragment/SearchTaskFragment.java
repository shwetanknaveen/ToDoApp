package com.example.todo3.ui.fragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import java.util.HashMap;
import java.util.List;
import java.util.Set;


public class SearchTaskFragment extends Fragment {

    private List<ToDoTask> mShowTasksList = new ArrayList<>();
    private DatabaseReference myRef;
    private FirebaseDatabase mDatabase;
    private RecyclerView mTaskList;
    private TextView mTextView;
    private String mSearchText;

    public SearchTaskFragment()
    {

    }
    public SearchTaskFragment(String searchText) {
        this.mSearchText = searchText;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_task, container, false);
        mTaskList = view.findViewById(R.id.searchTaskList);
        mTaskList.setLayoutManager(new LinearLayoutManager(getContext()));
        mTextView = view.findViewById(R.id.textViewSearchFrag);
        mDatabase = FirebaseDatabase.getInstance();
        myRef = mDatabase.getReference("Tasks");
        fetchData();
        return view;
    }
    public void fetchData()
    {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String, ToDoTask> map = (HashMap<String, ToDoTask>) dataSnapshot.getValue();
                Set<String> arrayList = map.keySet();   // verified with Toast that arrayList

                mShowTasksList.clear();     //to ensure that previously loaded tasks in recycler view don't get repeated

                for(String s : arrayList)
                {
                    if(dataSnapshot.child(s).getValue(ToDoTask.class).getTitle().toLowerCase().contains(mSearchText.trim().toLowerCase())||
                            dataSnapshot.child(s).getValue(ToDoTask.class).getDescription().toLowerCase().contains(mSearchText.trim().toLowerCase()))
                        mShowTasksList.add(dataSnapshot.child(s).getValue(ToDoTask.class));
                    //verified with toast that this list get all tasks from firebase

                }
                mTaskList.setAdapter(new TaskAdapter(mShowTasksList,getContext()));
                if(mShowTasksList.size()==0)
                    mTextView.setText("Search result is empty");
                else
                mTextView.setText("Search result for \""+mSearchText+"\"");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
