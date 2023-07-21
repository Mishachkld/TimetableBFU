package com.example.timetablebfu.ViewOfTable;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timetablebfu.R;

public class ViewHolder extends RecyclerView.ViewHolder {


    public ViewHolder(@NonNull View itemView) {
        super(itemView);
    }
}


/*private class MyAdapter extends BaseAdapter {

    // override other abstract methods here

    @Override
    public View getView(int position, View convertView, ViewGroup container) {
        if (convertView == null) {
            convertView = .getLayoutInflater().inflate(R.layout.main_activity, container, false);
        }

         convertView.findViewById(R.id.timetable_list);
        return convertView;
    }
}*/
