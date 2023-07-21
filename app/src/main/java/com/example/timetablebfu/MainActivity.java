package com.example.timetablebfu;

import android.app.Activity;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.Nullable;

import com.example.timetablebfu.Components.ScheduleList;
import com.example.timetablebfu.Constants.Constants;
import com.example.timetablebfu.GoogleSheetAPI.KeyAPI;
import com.example.timetablebfu.GoogleSheetAPI.SheetsWork;
import com.example.timetablebfu.ViewOfTable.ListAdapter;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private ListView list_timetable;
    private ArrayList<ScheduleList> res;
    private SheetsWork sheets;
    private List<List<Object>> valuesData;


//C:\Program Files\Java\jdk-14.0.1\bin

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        list_timetable = findViewById(R.id.timetable_list);



        // fill List of timetable
        res = new ArrayList<>();
        getDataSheets();
        /*for (int i = 0; i < 7; i++) {
            res.add(new ScheduleList(i + 1, Constants.WEEKDAY[i], Constants.LESSONS + "Ebal rot " + i));
        }*/



        /**ListView + ArrayAdapter**/
        ListAdapter listAdapter = new ListAdapter(this,res);
        list_timetable.setAdapter(listAdapter);
        list_timetable.setOnItemClickListener(new AdapterView.OnItemClickListener() {  /// сука посмотри про лямбды
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Snackbar.make(view, list_timetable.getItemAtPosition(i).toString(), Snackbar.LENGTH_LONG).show();
            }
        });
        /**ListView + ArrayAdapter**/


    }


    private void getDataSheets(){
        try {
            InputStream in = getAssets().open("credentials.json");
            sheets = new SheetsWork("2PM!A2:B2", KeyAPI.SPREADSHEET_ID, in);
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }
        valuesData = sheets.getValues();
        int i = 0;
        for (List row : valuesData) {
            res.add(new ScheduleList(i, Constants.WEEKDAY[i], row.get(i).toString()));
            i++;
        }
    }

}
