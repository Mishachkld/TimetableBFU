package com.example.timetablebfu.ViewOfTable;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
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
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_list_item, viewGroup, false);
        List<TextView> homeworkTextViewList = addTextViewsInLinearLayout(10, view);
        return new ViewHolder(view, homeworkTextViewList);
    }

    private List<TextView> addTextViewsInLinearLayout(int count, View view) {
        List<TextView> textViewList = new ArrayList<>();
        LinearLayout layout = view.findViewById(R.id.homework_LinearLayout);
        for (int i = 0; i < count; i++) {
            TextView text;
            if (i % 2 == 0) {
                text = generateTextViewWithTag(view, TypeOfTextView.LESSON, i);
            } else {
                text = generateTextViewWithTag(view, TypeOfTextView.HOMEWORK, i);
            }
            layout.addView(text);
            textViewList.add(text);
        }
        return textViewList;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        if (!res_list.isEmpty() && (position < homework.size()) && (position < lessons.size()) && (position < days.size())) {
            int indexLessons = 0;
            int indexHomework = 0;
            for (TextView textView : viewHolder.getHomeworkTextViewList()) {
                boolean isHomework = textView.getTag() == TypeOfTextView.HOMEWORK.toString();
                if (!isHomework) {
                    textView.setText(lessons.get(position).get(indexLessons % 5));
                    indexLessons++;
                } else {
                    textView.setText(homework.get(position).get(indexHomework % 5));
                    indexHomework++;
                }
            }
        }
        viewHolder.getDayOfWeakTextView().setText(days.get(position));
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
     * @param type  HOMEWORK or LESSON
     * @param index для индификации TextView
     * @return new TextView(); у которого тэг будет в виде: "HOMEWORK_" + index или "LESSON_" + "index"
     */
    private TextView generateTextViewWithTag(View view, TypeOfTextView type, int index) {
        TextView textView = new TextView(view.getContext());
        textView.setTextColor(Color.BLACK);
        textView.setTextSize(20);
        textView.setTag(type.toString());
        if (TypeOfTextView.LESSON == type) {
            textView.setTextSize(16);
            textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
        }

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

        private List<TextView> homeworkTextViewList;

        private List<TextView> lessonsTextViewList;

        private final TextView dayOfWeakTextView;

        public ViewHolder(View view, List<TextView> homeworkTextViewList, List<TextView> lessonsTextViewList) {
            this(view, homeworkTextViewList);
            this.lessonsTextViewList = lessonsTextViewList;
        }

        public ViewHolder(View view, List<TextView> homeworkTextViewList) {
            this(view);
            this.homeworkTextViewList = homeworkTextViewList;
        }

        public ViewHolder(View view) {
            super(view);
            homeworkTextViewList = new ArrayList<>();
            dayOfWeakTextView = view.findViewById(R.id.title_text);

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
        }

        public List<TextView> getHomeworkTextViewList() {
            return homeworkTextViewList;
        }

        public TextView getDayOfWeakTextView() {
            return dayOfWeakTextView;
        }

        public List<TextView> getLessonsTextViewList() {
            return lessonsTextViewList;
        }

    }
}