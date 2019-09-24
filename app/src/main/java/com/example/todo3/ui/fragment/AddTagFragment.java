package com.example.todo3.ui.fragment;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.todo3.R;
import com.example.todo3.adapter.TagAdapter;
import com.example.todo3.pojo.ToDoTag;
import com.example.todo3.utilities.Utility;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import yuku.ambilwarna.AmbilWarnaDialog;

public class AddTagFragment extends Fragment {
    private int mColor;
    private ToDoTag tagToAdd = new ToDoTag();
    private EditText mEditTextTitle;
    private Button mBtnSelectColor;
    private Button mBtnAddTag;
    private Button mBtnDeleteTag;
    private List<ToDoTag> mShowTagList= new ArrayList<>();
    private RecyclerView mTagList;

    public AddTagFragment() {
        mColor = -1;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_add_tag_fragment, container, false);
        mBtnSelectColor = view.findViewById(R.id.btnSelectColor);
        mBtnDeleteTag = view.findViewById(R.id.btnDeleteTag);
        mEditTextTitle = view.findViewById(R.id.editTextTagName);
        mBtnAddTag = view.findViewById(R.id.addTagBtn);
        mTagList = view.findViewById(R.id.tagList_manageTags);
        mTagList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mBtnSelectColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(getContext(), Color.rgb(1, 1, 1), new AmbilWarnaDialog.OnAmbilWarnaListener() {
                    @Override
                    public void onCancel(AmbilWarnaDialog dialog) {
                    }

                    @Override
                    public void onOk(AmbilWarnaDialog dialog, int color) {
                        mColor = color;
                        GradientDrawable gd = (GradientDrawable) mBtnSelectColor.getBackground().getCurrent();
                        gd.setColor(Color.parseColor(String.format("#%06X", (0xFFFFFF & color))));
                    }
                });
                colorPicker.show();
            }
        });
        mBtnAddTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mEditTextTitle.getText().toString().equals("") && mColor != -1) {
                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("Tags");
                    tagToAdd.setTagName(mEditTextTitle.getText().toString());
                    tagToAdd.setColor(String.format("#%06X", (0xFFFFFF & mColor)));
                    tagToAdd.setId(tagToAdd.hashCode());
                    myRef.child(Integer.toString(tagToAdd.getId())).setValue(tagToAdd);
                    Toast.makeText(getContext(), "Tag added", Toast.LENGTH_SHORT).show();
                    getActivity().onBackPressed();//go back to home screen
                } else
                    Toast.makeText(getContext(), "Either title or color missing!", Toast.LENGTH_LONG).show();
            }
        });

        FirebaseDatabase databaseForTags = FirebaseDatabase.getInstance();
        final DatabaseReference myRefForTags = databaseForTags.getReference("Tags");
        myRefForTags.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String, ToDoTag> map = (HashMap<String, ToDoTag>) dataSnapshot.getValue();
                Set<String> arrayList = map.keySet();
                mShowTagList.clear();
                for (String s : arrayList) {
                    mShowTagList.add(dataSnapshot.child(s).getValue(ToDoTag.class));
                }
                mTagList.setAdapter(new TagAdapter(mShowTagList, getContext()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mBtnDeleteTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Utility.toDoTag == null)
                {
                    Toast.makeText(getContext(), "No tag selected", Toast.LENGTH_SHORT).show();
                    return;
                }
                myRefForTags.child(Integer.toString(Utility.toDoTag.getId())).removeValue();
                Utility.toDoTag = null; //after using selected tag value in Utility class, we have to set it back to null
            }
        });

        return view;
    }//onCreateView() ends here


}
