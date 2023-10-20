package com.example.timetablebfu.ViewOfTable;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timetablebfu.Components.ScheduleList;
import com.example.timetablebfu.R;
import com.google.android.material.snackbar.Snackbar;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;

public class TimetableListAdapter extends RecyclerView.Adapter<TimetableListAdapter.ViewHolder> {

    private List<String> lessons;
    private List<String> homework;
    private List<String> days;
    private List<? extends ScheduleList> res_list;


    public TimetableListAdapter(List<ScheduleList> res) {
        this.res_list = res;
        getAllResources();
    }

    public TimetableListAdapter() {
        this.res_list = new ArrayList<>();
        getAllResources();
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.custom_list_item, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        if (!res_list.isEmpty() && (position < homework.size()) && (position < lessons.size()) && (position < days.size())) {
            viewHolder.getHomeWorkTextView().setText(homework.get(position));
            viewHolder.getLessonsTextView().setText(lessons.get(position));
            viewHolder.getDayOfWeakTextView().setText(days.get(position));
            // Get element from your dataset at this position and replace the
            // contents of the view with that element
        }
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return res_list.size();
    }

    private void getAllResources() {  // подумать как можно сделать через HashMap!!!
        //HashMap<String, String> oneDay = new HashMap<>();
        days = new ArrayList<>();
        lessons = new ArrayList<>();
        homework = new ArrayList<>();
        for (int i = 0; i < res_list.size(); i++) {
            days.add(res_list.get(i).getNameOfWeek());
            lessons.add(res_list.get(i).getLessons());
            homework.add(res_list.get(i).getHomeWork());
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateAdapter(List<ScheduleList> res_list) {
        this.res_list = res_list;
        getAllResources();
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView dayOfWeakTextView;
        private final TextView lessonsTextView;
        private final TextView homeWorkTextView;


        public ViewHolder(View view) {
            super(view);

            dayOfWeakTextView = view.findViewById(R.id.title_text);
            lessonsTextView = view.findViewById(R.id.lessons);
            homeWorkTextView = view.findViewById(R.id.homework);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, dayOfWeakTextView.getText(), Snackbar.LENGTH_LONG).show();
                }
            });
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Snackbar.make(view, "Element " + getAdapterPosition() + " clicked.", Snackbar.LENGTH_LONG).show();
                    return true;
                }
            });

            // Define click listener for the ViewHolder's View

        }

        public TextView getDayOfWeakTextView() {
            return dayOfWeakTextView;
        }

        public TextView getLessonsTextView() {
            return lessonsTextView;
        }

        public TextView getHomeWorkTextView() {
            return homeWorkTextView;
        }

    }


}
