package com.example.timetablebfu.GoogleSheetAPI;

import static com.example.timetablebfu.Constants.Constants.PREF_ACCOUNT_NAME;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.timetablebfu.Components.ScheduleList;
import com.example.timetablebfu.Constants.Constants;
import com.example.timetablebfu.GoogleSheetAPI.retrofit.APIService;
import com.example.timetablebfu.GoogleSheetAPI.retrofit.DataResponse;
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

public class SheetsWork {  /// Короче, нужно разобраться с Creditions, он позволяет облегчить запросы и т.д.
    // Делаем через GET запросы все.

    private static final String APPLICATION_NAME = "BFU Timetable";

    public static List<String> days;
    private String range;
    private String spreadsheetId;
    private static InputStream in;
    private List<ScheduleList> res;
    private Call<DataResponse> call;
    private APIService service;
    private Retrofit retrofit;


    private List<String> date = new ArrayList<>();
    private List<String> homework = new ArrayList<>();
    private List<String> lessons = new ArrayList<>();

    public SheetsWork(String range, String spreadsheetId, InputStream in) {
        this.range = range;
        this.spreadsheetId = spreadsheetId;
        SheetsWork.in = in;

    }

    public SheetsWork() {
        try {
            getData(getDataFromShit());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void defaultSettings() {
        retrofit = new Retrofit.Builder().baseUrl(APIConfig.URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        service = retrofit.create(APIService.class);
        date = new ArrayList<>();
        homework = new ArrayList<>();
        lessons = new ArrayList<>();
    }

    private static Retrofit getRetrofit(String url) {
        return new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();
    }


    private List<List<String>> getDataFromShit() throws IOException {  // вероятно лучше его сделать статическим, т.к. смысла от создания экземпляра класса особо нет
        Retrofit retrofit = getRetrofit(APIConfig.URL);
        APIService service = retrofit.create(APIService.class);
        Call<DataResponse> call = service.getData();
        List<List<String>> date = new ArrayList<>();
        call.enqueue(new Callback<DataResponse>() {
            @Override
            public void onResponse(@NonNull Call<DataResponse> call, @NonNull Response<DataResponse> response) {
                if (response.body() != null) {
                    date.addAll(response.body().values);
                    updateAdapter(date);
                }
            }
            @Override
            public void onFailure(Call<DataResponse> call, Throwable t) {
                Log.e("ERRROR", t.getMessage());
            }
        });

        return date;
    }

    private void updateAdapter(List<List<String>> date) {

    }

    private void enqueueCallFromSheet() { /// мне не нравится способ обновления данных. Даныне остаютс только внутри аноннимного внутреннего класса
        List<List<String>> data = new ArrayList<>();
        call.enqueue(new Callback<DataResponse>() {
            @Override
            public void onResponse(@NonNull Call<DataResponse> call, @NonNull Response<DataResponse> response) {
                if (response.body() != null) {
                    for (int i = 0; i < response.body().values.size(); i++) {
                        int counter = 0;
                        StringBuilder dataLessons = new StringBuilder();
                        for (int j = 0; j < response.body().values.get(i).size(); j++) {
                            String item = response.body().values.get(i).get(j);
                            switch (i) {
                                case 0:
                                    if (!item.equals(""))
                                        date.add(response.body().values.get(i).get(j));
                                    break;
                                case 1:
                                    counter++;
                                    dataLessons.append(counter).append(".").append(item).append("\n");
                                    if ((counter == Constants.RANGE) | (j == (response.body().values.get(i).size()) - 1)) {
                                        lessons.add(dataLessons.toString());
                                        counter = 0;
                                        dataLessons = new StringBuilder();
                                    }
                                    break;
                                case 2:
                                    counter++;
                                    dataLessons.append(counter).append(".").append(item).append("\n");
                                    if ((counter == Constants.RANGE) | (j == (response.body().values.get(i).size()) - 1)) {
                                        homework.add(dataLessons.toString());
                                        counter = 0;
                                        dataLessons = new StringBuilder();
                                    }
                                    break;
                            }
                        }
                    }
                    while (date.size() > lessons.size())
                        lessons.add("Not Found 404");
                    while (date.size() > homework.size())
                        homework.add("Not Found 404");
                    data.add(date);
                    data.add(lessons);
                    data.add(homework);
                    for (int i = 0; i < lessons.size(); i++)
                        res.add(new ScheduleList(i, date.get(i), lessons.get(i), homework.get(i)));
                    //setRecyclerView(res);  /// это как то вообще не праивльно выглядит, но по другому оно не работает, все элементы массивов исчезают

                    getData(data);
                }

            }

            @Override
            public void onFailure(Call<DataResponse> call, Throwable t) {


            }
        });
    }

    private void getData(List<List<String>> data) {
        res = new ArrayList<>();
        for (int i = 0; i < data.size(); i++)
            switch (i) {
                case 0:
                    date = data.get(i);
                    break;
                case 1:
                    lessons = data.get(i);
                    break;
                case 2:
                    homework = data.get(i);
                    break;
            }
        for (int i = 0; i < date.size(); i++)
            res.add(new ScheduleList(i, date.get(i), lessons.get(i), homework.get(i)));
    }


    public List<ScheduleList> getValues() {
        /*if (res == null || res.isEmpty()) {
            throw new IOException("Not found Data");
        }*/
        return res;
    }

    public List<String> getDate() {
        return date;
    }

    public List<String> getHomework() {
        return homework;
    }

    public List<String> getLessons() {
        return lessons;
    }


    private void listDataSort(List<List<String>> data) {
        for (int i = 0; i < data.size(); i++) {
            int counter = 0;
            StringBuilder dataLessons = new StringBuilder();
            for (int j = 0; j < data.get(i).size(); j++) {
                String item = data.get(i).get(j);
                switch (i) {
                    case 0:
                        if (!item.equals(""))
                            date.add(data.get(i).get(j));
                        break;
                    case 1:
                        counter++;
                        dataLessons.append(counter).append(".").append(item).append("\n");
                        if ((counter == Constants.RANGE) | (j == (data.get(i).size()) - 1)) {
                            lessons.add(dataLessons.toString());
                            counter = 0;
                            dataLessons = new StringBuilder();
                        }
                        break;
                    case 2:
                        counter++;
                        dataLessons.append(counter).append(".").append(item).append("\n");
                        if ((counter == Constants.RANGE) | (j == (data.get(i).size()) - 1)) {
                            homework.add(dataLessons.toString());
                            counter = 0;
                            dataLessons = new StringBuilder();
                        }
                        break;
                }
            }
        }
        while (date.size() > lessons.size())
            lessons.add("Not Found 404");
        while (date.size() > homework.size())
            homework.add("Not Found 404");
        data.add(date);
        data.add(lessons);
        data.add(homework);
        for (int i = 0; i < lessons.size(); i++)
            res.add(new ScheduleList(i, date.get(i), lessons.get(i), homework.get(i)));
        //setRecyclerView(res);  /// это как то вообще не праивльно выглядит, но по другому оно не работает, все элементы массивов исчезают

        getData(data);
    }
}

