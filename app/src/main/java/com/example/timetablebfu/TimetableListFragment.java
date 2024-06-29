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
    private SwipeRefreshLayout swipe;
    private TimetableListAdapter adapter;
    private RecyclerView recyclerView;
    private SheetsWork sheetsWork;

    public TimetableListFragment() {
        super(R.layout.fragment_listview);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_listview, container, false);
        if (rootView != null) {
            recyclerView = rootView.findViewById(R.id.timetable_list);
            swipe = rootView.findViewById(R.id.swiperefresh);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            swipe.setRefreshing(true);
            adapter = new TimetableListAdapter();
            recyclerView.setAdapter(adapter);

            res = new ArrayList<>();
            sheetsWork = new SheetsWork();
            sheetsWork.getDataFromSheet(swipe, recyclerView, adapter, res);
            swipe.setOnRefreshListener(() -> sheetsWork.getDataFromSheet(swipe, recyclerView, adapter, res));
        }
        return rootView;
    }
}