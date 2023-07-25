package com.example.timetablebfu;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.Nullable;


import com.example.timetablebfu.Components.ScheduleList;
import com.example.timetablebfu.Constants.Constants;
import com.example.timetablebfu.GoogleSheetAPI.APIConfig;
import com.example.timetablebfu.GoogleSheetAPI.retrofit.APIService;
import com.example.timetablebfu.GoogleSheetAPI.retrofit.DataResponse;
import com.example.timetablebfu.GoogleSheetAPI.KeyAPI;
import com.example.timetablebfu.GoogleSheetAPI.SheetsWork;
import com.example.timetablebfu.ViewOfTable.CustomListAdapter;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;

public class MainActivity extends Activity {
    private ListView list_timetable;
    private List<List<Object>> valuesData;
    private SheetsWork sheets;
    private ArrayList<ScheduleList> res = new ArrayList<ScheduleList>();
    private APIService service;
    Call<DataResponse> call;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        list_timetable = findViewById(R.id.timetable_list);

       String url = APIConfig.URL+"v4/spreadsheets/"+ APIConfig.SPREADSHEET_ID+"/values/"+APIConfig.SPREADSHEET_LIST_ID + APIConfig.SPREADSHEET_MAJOR_DIMENSION+"&key="+ KeyAPI.GOOGLE_API_KEY;

        Retrofit retrofit = new Retrofit.Builder().baseUrl(APIConfig.URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        service = retrofit.create(APIService.class);

        loadData();



    }

    private void loadData() {
        List<String> days = new ArrayList<String>();
        call = service.getData();
        call.enqueue(new Callback<DataResponse>() {
            @Override
            public void onResponse(Call<DataResponse> call, Response<DataResponse> response) {
                if (response.body() != null) {
                    for (int i = 0; i < 3; i++) { ///
                        for (int j = 0; j < response.body().values.get(i).size(); j++) {
                            days.add(Constants.WEEKDAY[j % 7]);
                            res.add(new ScheduleList(days.size(), Constants.WEEKDAY[i % 7], response.body().values.get(i).get(j)));
                        }
                    }
                    setAdapter(days);
                }
            }

            @Override
            public void onFailure(Call<DataResponse> call, Throwable t) {
                Log.println(Log.ERROR, "MainActivity", "errorrrrrrrrrrrrrr");
            }
        });
    }

    private void setAdapter(List<String> days) {
        /**ListView + ArrayAdapter**/
        CustomListAdapter listAdapter = new CustomListAdapter(this, res, days);
        list_timetable.setAdapter(listAdapter);
        list_timetable.setOnItemClickListener(new AdapterView.OnItemClickListener() {  /// сука посмотри про лямбды
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Snackbar.make(view, list_timetable.getItemAtPosition(i).toString(), Snackbar.LENGTH_LONG).show();
            }
        });
        /**ListView + ArrayAdapter**/
    }


    private void getDataSheets() {
        try {
            InputStream in = getAssets().open("credentials.json");
            sheets = new SheetsWork("2PM!A2:B2", APIConfig.SPREADSHEET_ID, in);
            valuesData = sheets.getValues();
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }
        int i = 0;
        for (List row : valuesData) {
            res.add(new ScheduleList(i, Constants.WEEKDAY[i], row.get(i).toString()));
            i++;
        }
    }

}
