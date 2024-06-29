package com.example.timetablebfu.GoogleSheetAPI;

import static com.example.timetablebfu.Constants.Constants.PREF_ACCOUNT_NAME;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.timetablebfu.Components.ScheduleList;
import com.example.timetablebfu.Constants.Constants;
import com.example.timetablebfu.GoogleSheetAPI.retrofit.APIService;
import com.example.timetablebfu.GoogleSheetAPI.retrofit.DataResponse;
import com.example.timetablebfu.ViewOfTable.TimetableListAdapter;
import com.google.android.material.snackbar.Snackbar;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.api.services.tasks.TasksScopes;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SheetsWork {
    private final APIService service;

    public SheetsWork() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIConfig.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(APIService.class);
    }

    public void getDataFromSheet(SwipeRefreshLayout swipe, RecyclerView recyclerView, TimetableListAdapter adapter, List<ScheduleList> res) {
        ArrayList<String> date = new ArrayList<>();
        ArrayList<String> homework = new ArrayList<>();
        List<String> lessons = new ArrayList<>();
        List<List<String>> data = new ArrayList<>();


        Call<DataResponse> call = service.getData(APIConfig.SPREADSHEET_LIST_ID);
        call.enqueue(new Callback<DataResponse>() {
            @Override
            public void onResponse(@NonNull Call<DataResponse> call, @NonNull Response<DataResponse> response) {
                if (response.body() != null) {
                    processData(response.body().values, date, lessons, homework);
                    data.add(date);
                    data.add(lessons);
                    data.add(homework);
                    swipe.setRefreshing(false);
                    updateRecyclerView(data, res, adapter);
                }
            }

            @Override
            public void onFailure(Call<DataResponse> call, Throwable t) {
                Snackbar.make(recyclerView, "Error...", Snackbar.LENGTH_LONG).show();
                swipe.setRefreshing(false);
            }
        });
    }

    private void processData(List<List<String>> values, List<String> date, List<String> lessons, List<String> homework) {
        for (int i = 0; i < values.size(); i++) {
            int counter = 0;
            StringBuilder dataBuilder = new StringBuilder();
            for (int j = 0; j < values.get(i).size(); j++) {
                String item = values.get(i).get(j);
                switch (i) {
                    case 0:
                        if (!item.isEmpty())
                            date.add(item);
                        break;
                    case 1:
                    case 2:
                        counter++;
                        dataBuilder.append(counter).append(".").append(item).append("\n");
                        if (counter == Constants.RANGE || j == values.get(i).size() - 1) {
                            if (i == 1) {
                                lessons.add(dataBuilder.toString());
                            } else {
                                homework.add(dataBuilder.toString());
                            }
                            counter = 0;
                            dataBuilder = new StringBuilder();
                        }
                        break;
                }
            }
        }
        while (date.size() > lessons.size())
            lessons.add("Not Found 404");
        while (date.size() > homework.size())
            homework.add("Not Found 404");
    }

    private void updateRecyclerView(List<List<String>> data, List<ScheduleList> res, TimetableListAdapter adapter) {
        List<String> date = data.get(0);
        List<String> lessons = data.get(1);
        List<String> homework = data.get(2);

        for (int i = 0; i < date.size(); i++) {
            res.add(new ScheduleList(i, date.get(i), lessons.get(i), homework.get(i)));
        }
        adapter.updateAdapter(res);
    }
}