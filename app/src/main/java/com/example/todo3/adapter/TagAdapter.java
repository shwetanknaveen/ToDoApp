package com.example.todo3.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo3.R;
import com.example.todo3.pojo.ToDoTag;
import com.example.todo3.utilities.Utility;

import java.util.ArrayList;
import java.util.List;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.TagViewHolder> {

    private List<ToDoTag> mTags;
    private Context mContext;
    private GradientDrawable mGd;
    private List<ToDoTag> selectedTags;

    public TagAdapter(List<ToDoTag> tags, Context context) {
        this.mTags = tags;
        this.mContext = context;
        selectedTags = new ArrayList<>();
        selectedTags.clear();
    }

    @NonNull
    @Override
    public TagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.tag_item_layout, parent, false);
        return new TagViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TagViewHolder holder, final int position) {
        holder.textViewTagItem.setText(mTags.get(position).getTagName());

        mGd = (GradientDrawable) holder.textViewTagItem.getBackground().getCurrent();

        if (!TextUtils.isEmpty(mTags.get(position).getColor()))
            mGd.setColor(Color.parseColor(mTags.get(position).getColor()));
        else
            mGd.setColor(Color.parseColor("#123455"));//setting default color

        if(!selectedTags.contains(mTags.get(position))) //if selected tag list does not contain given tag then don't draw tick icon
            holder.textViewTagItem.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        else
            holder.textViewTagItem.setCompoundDrawablesWithIntrinsicBounds(R.drawable.tag_tick_icon, 0, 0, 0);


        holder.textViewTagItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedTags.contains(mTags.get(position))) {    // it means particular tag is already selected
                    holder.textViewTagItem.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    selectedTags.remove(mTags.get(position));   //now tag is unselected
                }
                else
                {
                    holder.textViewTagItem.setCompoundDrawablesWithIntrinsicBounds(R.drawable.tag_tick_icon, 0, 0, 0);
                    selectedTags.add(mTags.get(position));
                }
                Toast.makeText(mContext, holder.textViewTagItem.getText() + " Selected", Toast.LENGTH_SHORT).show();
                Utility.toDoTag = mTags.get(position);      //now tag is selected

            }
        });
    }

    @Override
    public int getItemCount() {
        return mTags.size();
    }

    public class TagViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTagItem;

        public TagViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTagItem = itemView.findViewById(R.id.textViewTagItem);
        }
    }
}
