package com.example.timetablebfu.ViewOfTable;

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
import java.util.HashMap;

public class ListAdapter extends ArrayAdapter<String> {
    private Activity context;
    private String[] days;
    private String[] lessons;
    private int[] id;
    private ArrayList<? extends ScheduleList> list;


    public ListAdapter(Activity context, String[] days, String lessons, int[] id) {
        super(context, R.layout.custom_list_item, days);
        this.context = context;
        this.days = days;
        this.id = id;
    }

    public ListAdapter(Activity context, ArrayList<ScheduleList> res) {
        super(context, R.layout.custom_list_item, Constants.WEEKDAY);
        this.list = res;
        this.context = context;
        getAllResources();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();  // получаем макет
        View rowView = inflater.inflate(R.layout.custom_list_item, null, true);  // хуй знает что мы делаем (навеное раздуваем разметку)

        AppCompatTextView num = rowView.findViewById(R.id.title_position);
        TextView titleText = rowView.findViewById(R.id.title_text);
        TextView subtitleText = rowView.findViewById(R.id.lessons);

        titleText.setText(days[position]);
        subtitleText.setText(lessons[position]);
        num.setText(new StringBuilder(id[position] + "."));

        return rowView;
    }

    private void getAllResources() {  // подумать как можно сделать через HashMap!!!
//        HashMap<String, String> oneDay = new HashMap<>();
        days = new String[list.size()];
        lessons = new String[list.size()];
        id = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            days[i] = list.get(i).getNameOfWeek();
            lessons[i] = list.get(i).getHomeWork();
            id[i] = list.get(i).getId();
        }
    }


}
