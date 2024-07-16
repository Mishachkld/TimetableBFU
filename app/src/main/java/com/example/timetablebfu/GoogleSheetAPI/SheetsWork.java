package com.example.timetablebfu.GoogleSheetAPI;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.timetablebfu.Components.ScheduleTable;
import com.example.timetablebfu.Constants.Constants;
import com.example.timetablebfu.GoogleSheetAPI.retrofit.APIService;
import com.example.timetablebfu.GoogleSheetAPI.retrofit.DataResponse;
import com.example.timetablebfu.ViewOfTable.TimetableListAdapter;
import com.google.android.material.snackbar.Snackbar;

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

    public void getDataFromSheet(SwipeRefreshLayout swipe, RecyclerView recyclerView, TimetableListAdapter adapter, List<ScheduleTable> res) {
        List<String> date = new ArrayList<>();
        List<List<String>> homework = new ArrayList<>();
        List<List<String>> lessons = new ArrayList<>();


        Call<DataResponse> call = service.getData(APIConfig.SPREADSHEET_LIST_ID);
        call.enqueue(new Callback<DataResponse>() {
            @Override
            public void onResponse(@NonNull Call<DataResponse> call, @NonNull Response<DataResponse> response) {
                if (response.body() != null) {
                    processData(response.body().values, date, lessons, homework);

                    swipe.setRefreshing(false);
                    updateView(date, lessons, homework, res, adapter);
                }
            }

            @Override
            public void onFailure(Call<DataResponse> call, Throwable t) {
                Snackbar.make(recyclerView, "Error...", Snackbar.LENGTH_LONG).show();
                swipe.setRefreshing(false);
            }
        });
    }

    private void processData(List<List<String>> values, List<String> date, List<List<String>> lessons, List<List<String>> homework) {
        lessons.add(new ArrayList<>());
        homework.add(new ArrayList<>());
        for (int indexColumn = 0; indexColumn < values.size(); indexColumn++) {
            int counter = 0;
            for (int indexRow = 0; indexRow < values.get(indexColumn).size(); indexRow++) {
                String item = values.get(indexColumn).get(indexRow);
                switch (indexColumn) {
                    case 0:
                        if (!item.isEmpty()) {
                            date.add(item);
                        }
                        break;
                    case 1:
                        counter++;
                        String nameOfLesson = counter + ". " + item;
                        lessons.get(lessons.size() - 1).add(nameOfLesson);
                        if ((counter == Constants.RANGE) | (indexRow == (values.get(indexColumn).size()) - 1)) {
                            lessons.add(new ArrayList<>());
                            counter = 0;
                        }
                        break;
                    case 2:
                        counter++;
                        String nameOfHomeWork = item;
                            homework.get(homework.size() - 1).add(nameOfHomeWork);
                            if ((counter == Constants.RANGE) | (indexRow == (values.get(indexColumn).size()) - 1)) {
                                homework.add(new ArrayList<>());
                                counter = 0;
                            }
                        break;

                }
            }
        }
        fillArray(lessons, homework, 5,  date.size());

    }

    private void fillArray(List<List<String>> lessons, List<List<String>> homework, int count, int sizeOfArray){
        List<String> lastLessons = lessons.get(lessons.size() - 2);
        List<String> lastHomework = homework.get(homework.size() - 2);
       while (lastLessons.size() < count){
           lastLessons.add("Not Found 404");
       }
       while (lastHomework.size() < count){
           lastHomework.add("Not Found 404");
       }

        while (sizeOfArray > lessons.size())
            lessons.add(new ArrayList<>(Collections.nCopies(count, "Not Found 404")));
        while (sizeOfArray > homework.size())
            homework.add(new ArrayList<>(Collections.nCopies(count, "Not Found 404")));
    }

    private void updateView(List<String> date, List<List<String>> lessons, List<List<String>> homework, List<ScheduleTable> res, TimetableListAdapter adapter) {
        for (int i = 0; i < date.size(); i++) {
            res.add(new ScheduleTable(i, date.get(i), lessons.get(i), homework.get(i)));
        }
        adapter.updateAdapter(res);
    }
}