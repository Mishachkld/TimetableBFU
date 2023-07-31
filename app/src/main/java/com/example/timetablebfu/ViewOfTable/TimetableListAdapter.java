package com.example.timetablebfu.ViewOfTable;

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

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;

public class TimetableListAdapter extends RecyclerView.Adapter<TimetableListAdapter.ViewHolder> {

    private ArrayList<String> lessons;
    private ArrayList<String> homework;
    private ArrayList<String> days;
    private final ArrayList<? extends ScheduleList> res_list;


    public TimetableListAdapter(ArrayList<ScheduleList> res) {
        this.res_list = res;
        getAllResources();
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


    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.custom_list_item, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        if (!res_list.isEmpty()) {
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

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView dayOfWeakTextView;
        private final TextView lessonsTextView;
        private final TextView homeWorkTextView;



        public ViewHolder(View view) {
            super(view);
            dayOfWeakTextView = view.findViewById(R.id.title_text);
            lessonsTextView = view.findViewById(R.id.lessons);
            homeWorkTextView = view.findViewById(R.id.homework);
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
