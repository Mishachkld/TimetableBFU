package com.example.timetablebfu.ViewOfTable;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.example.timetablebfu.Components.ScheduleList;
import com.example.timetablebfu.Constants.Constants;
import com.example.timetablebfu.R;

import java.util.ArrayList;
import java.util.List;

public class CustomListAdapter extends ArrayAdapter<String> {
    private Activity context;
    private List<String> days;
    //    private String[] lessons;
    private String[] lessons;
    private String[] homework;
    private int[] id;
    private ArrayList<? extends ScheduleList> list;


    public CustomListAdapter(Activity context, ArrayList<ScheduleList> res, List<String> days) {
        super(context, R.layout.custom_list_item, days);
        this.context = context;
        this.list = res;
        this.days = days;
        getAllResources();
    }

    public CustomListAdapter(Activity context, ArrayList<ScheduleList> res) {
        super(context, R.layout.custom_list_item, Constants.WEEKDAY);
        this.list = res;
        this.context = context;
        getAllResources();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();  // получаем макет
            @SuppressLint({"ViewHolder", "InflateParams"}) View rowView = inflater.inflate(R.layout.custom_list_item, null, true);  // хуй знает что мы делаем (навеное раздуваем разметку)

        TextView num = rowView.findViewById(R.id.title_position);
        TextView titleText = rowView.findViewById(R.id.title_text);
        TextView homeWorkText = rowView.findViewById(R.id.homework);
        TextView lessonsText = rowView.findViewById(R.id.lessons);

        if (!list.isEmpty()) {
            titleText.setText(days.get(position));
            lessonsText.setText(lessons[position]);
            homeWorkText.setText(homework[position]);
//            num.setText(new StringBuilder(id[position] + "."));
            num.setText("");
        }
        return rowView;
    }

    private void getAllResources() {  // подумать как можно сделать через HashMap!!!
        //HashMap<String, String> oneDay = new HashMap<>();
        //days = new String[list.size()];
        homework = new String[list.size()];
        lessons = new String[list.size()];
        id = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            //days[i] = list.get(i).getNameOfWeek();
            lessons[i] = list.get(i).getLessons();
            homework[i] = list.get(i).getHomeWork();
            id[i] = list.get(i).getId();
        }
    }


}
