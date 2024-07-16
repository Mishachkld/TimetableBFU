package com.example.timetablebfu.ViewOfTable;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timetablebfu.Components.ScheduleTable;
import com.example.timetablebfu.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class TimetableListAdapter extends RecyclerView.Adapter<TimetableListAdapter.ViewHolder> {

    private List<List<String>> lessons;
    private List<List<String>> homework;
    private List<String> days;
    private final List<ScheduleTable> res_list;

    public TimetableListAdapter(List<ScheduleTable> res) {
        this.res_list = res;
        getAllResources();
    }

    public TimetableListAdapter() {
        this(new ArrayList<>());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // можно создать отдельно LinerLayout для уроков и для домашки
        // тогда мы будем отдельно добавлять элементы для обоих Liner'ов
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_list_item, viewGroup, false);
        List<TextView> homeworkTextViewList = addTextViewsInLinearLayout(5, view);
        return new ViewHolder(view, homeworkTextViewList);
    }

    private List<TextView> addTextViewsInLinearLayout(int count, View view) {
        List<TextView> textViewList = new ArrayList<>();
        LinearLayout layout = view.findViewById(R.id.lessons_LinearLayout);
        for (int i = 0; i < count; i++) {
            TextView text = generateTextViewWithTag(view,  TypeOfTextView.LESSON, 0);
            layout.addView(text);
            textViewList.add(text);
        }
        return textViewList;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        if (!res_list.isEmpty() && (position < homework.size()) && (position < lessons.size()) && (position < days.size())) {
            for (int i = 0; i < lessons.get(position).size(); i++) {
                viewHolder.getDataTextView().get(i).setText(lessons.get(position).get(i));
            }
            viewHolder.getDayOfWeakTextView().setText(days.get(position));
        }
    }

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
            days.add(res_list.get(i).getDate());
            lessons.add(res_list.get(i).getLessonsList());
            homework.add(res_list.get(i).getHomeworksList());
        }
    }


    // textView.setTag() ? "HOMEWORK_0" : "LESSON_0" - пример тэга

    /**
     * @param view
     * @param type HOMEWORK or LESSON
     * @param index для индификации TextView
     * @return new TextView(); у которого тэг будет в виде: "HOMEWORK_" + index или "LESSON_" + "index"
     */
    private TextView generateTextViewWithTag(View view, TypeOfTextView type, int index) {
        TextView textView = new TextView(view.getContext());
        textView.setTextSize(16);
        textView.setTextColor(Color.BLACK);
        textView.setTag(type.toString() + "_" + index);
        return textView;
    }

    private enum TypeOfTextView {
        HOMEWORK, LESSON
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateAdapter(List<ScheduleTable> newData) {
        res_list.clear();
        res_list.addAll(newData);
        getAllResources();
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private List<TextView> dataTextView;

        private final TextView dayOfWeakTextView;
        private final TextView lessonsTextView;
        private final TextView homeWorkTextView;

        public ViewHolder(View view, List<TextView> dataTextView){
            this(view);
            this.dataTextView = dataTextView;
        }

        public ViewHolder(View view) {
            super(view);
            dataTextView = new ArrayList<>();
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

        public List<TextView> getDataTextView(){ return dataTextView; }

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
