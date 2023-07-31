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
import com.example.timetablebfu.GoogleSheetAPI.retrofit.APIService;
import com.example.timetablebfu.GoogleSheetAPI.retrofit.DataResponse;
import com.example.timetablebfu.ViewOfTable.CustomListAdapter;
import com.example.timetablebfu.ViewOfTable.TimetableListAdapter;
import com.google.android.material.snackbar.Snackbar;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class TimetableListFragment extends Fragment {
    private ArrayList<ScheduleList> res;
    private APIService service;
    private Call<DataResponse> call;
    private Retrofit retrofit;

    private SwipeRefreshLayout swipe;
    private ListView list_timetable;

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
            Retrofit retrofit = new Retrofit.Builder().baseUrl(APIConfig.URL)
                    .addConverterFactory(GsonConverterFactory.create()).build();
            service = retrofit.create(APIService.class);
            swipe.setRefreshing(true);
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

    public void setService4Sheet(APIService service,  Retrofit retrofit) {
        this.service = service;
        this.retrofit = retrofit;
    }

    @Override
    public void onStart() {
        super.onStart();
        /*View view = getView();
        if (view != null) {
            //list_timetable = view.findViewById(R.id.timetable_list);
            recyclerView = view.findViewById(R.id.timetable_list);
            swipe = view.findViewById(R.id.swiperefresh);

            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            Retrofit retrofit = new Retrofit.Builder().baseUrl(APIConfig.URL)
                    .addConverterFactory(GsonConverterFactory.create()).build();
            service = retrofit.create(APIService.class);
            swipe.setRefreshing(true);
            getDataFromShit();
            swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    getDataFromShit();
                }
            });


        }*/
    }

    private void getDataFromShit() {  /// мне не нравится способ обновления данных. Даныне остаютс только внутри аноннимного внутреннего класса
        List<String> date = new ArrayList<>();
        List<String> homework = new ArrayList<>();
        List<String> lessons = new ArrayList<>();
        res = new ArrayList<>();


        //call = service.getData(APIConfig.SPREADSHEET_LIST_ID, APIConfig.SPREADSHEET_MAJOR_DIMENSION);
        call = service.getData();
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
                    for (int i = 0; i < lessons.size(); i++)
                        res.add(new ScheduleList(i, date.get(i), lessons.get(i), homework.get(i)));
                    swipe.setRefreshing(false);
                    setRecyclerView(res);  /// это как то вообще не праивльно выглядит, но по другому оно не работает, все элементы массивов исчезают
                }

            }

            @Override
            public void onFailure(Call<DataResponse> call, Throwable t) {
                Snackbar.make(list_timetable, "Errrorrr....", Snackbar.LENGTH_LONG).show();
                swipe.setRefreshing(false);

            }
        });

    }

    private void setArrayAdapter(List<String> days) {
        /**ListView + ArrayAdapter**/
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
        /**ListView + ArrayAdapter**/
    }

    private void setRecyclerView(ArrayList<ScheduleList> res) {
        TimetableListAdapter adapter = new TimetableListAdapter(res);
        recyclerView.setAdapter(adapter);
    }

}