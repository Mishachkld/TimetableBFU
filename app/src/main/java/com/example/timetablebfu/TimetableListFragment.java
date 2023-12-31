package com.example.timetablebfu;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.timetablebfu.Components.ScheduleList;
import com.example.timetablebfu.Constants.Constants;
import com.example.timetablebfu.GoogleSheetAPI.APIConfig;
import com.example.timetablebfu.GoogleSheetAPI.SheetsWork;
import com.example.timetablebfu.GoogleSheetAPI.retrofit.APIService;
import com.example.timetablebfu.GoogleSheetAPI.retrofit.DataResponse;
import com.example.timetablebfu.ViewOfTable.TimetableListAdapter;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class TimetableListFragment extends Fragment {
    private List<ScheduleList> res;
    private APIService service;
    private SwipeRefreshLayout swipe;
    private Call<DataResponse> call;
    private Retrofit retrofit;
    private TimetableListAdapter adapter;
    private List<String> date;
    private List<String> homework;
    private List<String> lessons;
    private RecyclerView recyclerView;

    public TimetableListFragment() {
        super(R.layout.fragment_listview);
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_listview, container, false);
        if (rootView != null) {
            recyclerView = rootView.findViewById(R.id.timetable_list);
            swipe = rootView.findViewById(R.id.swiperefresh);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            swipe.setRefreshing(true);
            adapter = new TimetableListAdapter();
            recyclerView.setAdapter(adapter);

            res = new ArrayList<>();
            retrofit = new Retrofit.Builder().baseUrl(APIConfig.URL)
                    .addConverterFactory(GsonConverterFactory.create()).build();
            service = retrofit.create(APIService.class);
            SheetsWork dataSheets = new SheetsWork();
            getDataFromShit();
            swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    getDataFromShit();
                }
            });

        }
        return rootView;
    }

    private void getDataFromShit() {  /// мне не нравится способ обновления данных. Даныне остаются только внутри аноннимного внутреннего класса
        ArrayList<String> date = new ArrayList<>();
        ArrayList<String> homework = new ArrayList<>();
        ArrayList<String> lessons = new ArrayList<>();
        List<List<String>> data = new ArrayList<>();


        call = service.getData(APIConfig.SPREADSHEET_LIST_ID);
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
                   /* for (int i = 0; i < lessons.size(); i++)
                        res.add(new ScheduleList(i, date.get(i), lessons.get(i), homework.get(i)));*/
                    //setRecyclerView(res);  /// это как то вообще не праивльно выглядит, но по другому оно не работает, все элементы массивов исчезают
                    swipe.setRefreshing(false);
                    getData(data);
                }

            }

            @Override
            public void onFailure(Call<DataResponse> call, Throwable t) {
                Snackbar.make(recyclerView, "Errrorrr....", Snackbar.LENGTH_LONG).show();
                swipe.setRefreshing(false);

            }
        });
        //setRecyclerView(res);
    }

    private void getData(List<List<String>> data) {
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
        setRecyclerView(res);
    }

    /*private void setArrayAdapter(List<String> days) {
     *//**ListView + ArrayAdapter**//*
        if ((res != null) && !res.isEmpty()) {
            CustomListAdapter listAdapter = new CustomListAdapter(getActivity(), res, days);
            list_timetable.setAdapter(listAdapter);
            list_timetable.setOnItemClickListener(new AdapterView.OnItemClickListener() {  /// сука посмотри про лямбды
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Snackbar.make(view, list_timetable.getItemAtPosition(i).toString(), Snackbar.LENGTH_LONG).show();
                }
            });
        } else
            Snackbar.make(list_timetable, "Data not found", Snackbar.LENGTH_LONG).show();
        */

    /**
     * ListView + ArrayAdapter
     **//*
    }
*/
    private void setRecyclerView(List<ScheduleList> res) {
        adapter.updateAdapter(res);
    }

}