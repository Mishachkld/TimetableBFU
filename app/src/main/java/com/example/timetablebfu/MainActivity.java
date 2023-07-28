package com.example.timetablebfu;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.example.timetablebfu.Components.ScheduleList;
import com.example.timetablebfu.Constants.Constants;
import com.example.timetablebfu.GoogleSheetAPI.APIConfig;
import com.example.timetablebfu.GoogleSheetAPI.retrofit.APIService;
import com.example.timetablebfu.GoogleSheetAPI.retrofit.DataResponse;
import com.example.timetablebfu.GoogleSheetAPI.SheetsWork;
import com.example.timetablebfu.ViewOfTable.CustomListAdapter;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;

public class MainActivity extends Activity {
    private ListView list_timetable;
    private SwipeRefreshLayout swipe;
    private SheetsWork sheets;
    private List<List<Object>> valuesData;
    private ArrayList<ScheduleList> res = new ArrayList<ScheduleList>();
    private APIService service;
    Call<DataResponse> call;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        list_timetable = findViewById(R.id.timetable_list);
        swipe = findViewById(R.id.swiperefresh);

        Retrofit retrofit = new Retrofit.Builder().baseUrl(APIConfig.URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        service = retrofit.create(APIService.class);
        swipe.setRefreshing(true);
        loadSortedData();
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadSortedData();
            }
        });


    }

    private void loadSortedData() {
        SheetsWork sh = new SheetsWork();
        getDataGson();
        swipe.setRefreshing(false);
        /*if (res != null)
            setAdapter(sh.getDate());*/
    }

    private void loadData() {
        List<String> days = new ArrayList<String>();
        call = service.getData();
        call.enqueue(new Callback<DataResponse>() {
            @Override
            public void onResponse(Call<DataResponse> call, Response<DataResponse> response) {
                if (response.body() != null) {
                    for (int i = 0; i < response.body().values.size(); i++) { ///
                        for (int j = 0; j < response.body().values.get(i).size(); j++) {
                            days.add(Constants.WEEKDAY[j % 7]);
                            res.add(new ScheduleList(days.size(), Constants.WEEKDAY[i % 7], response.body().values.get(i).get(j)));
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<DataResponse> call, Throwable t) {
                Snackbar.make(list_timetable, "Errrorrrrrr...", Snackbar.LENGTH_LONG).show();
            }
        });
        swipe.setRefreshing(false);
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

    private void getDataGson() {  /// оно почему то не работает, дата не собирается
        List<String> date = new ArrayList<>();
        List<String> homework = new ArrayList<>();
        List<String> lessons = new ArrayList<>();
        res = new ArrayList<>();

        call = service.getData();
        call.enqueue(new Callback<DataResponse>() {
            @Override
            public void onResponse(Call<DataResponse> call, Response<DataResponse> response) {
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
                                    dataLessons.append(item).append("\n");
                                    counter++;
                                    if ((counter == 5) | (j == (response.body().values.get(i).size()) - 1)) {
                                        homework.add(dataLessons.toString());
                                        counter = 0;
                                        dataLessons = new StringBuilder();
                                    }
                                    break;
                                case 2:
                                    dataLessons.append(item).append("\n");
                                    counter++;
                                    if ((counter == 5) | (j == (response.body().values.get(i).size()) - 1)) {
                                        lessons.add(dataLessons.toString());
                                        counter = 0;
                                        dataLessons = new StringBuilder();
                                    }
                                    break;
                            }
                        }
                    }

                    while (date.size() > lessons.size())
                        lessons.add("Not Found 404");
                    for (int i = 0; i < lessons.size(); i++) {
                        res.add(new ScheduleList(i, date.get(i), lessons.get(i)));
                    }

                    setAdapter(date);
                }
            }
                @Override
                public void onFailure (Call < DataResponse > call, Throwable t){
                    Snackbar.make(list_timetable, "Errrorrr....", Snackbar.LENGTH_LONG).show();
                }
            });

        swipe.setRefreshing(false);
        }


    }
