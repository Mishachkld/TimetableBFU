package com.example.timetablebfu;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.timetablebfu.Components.ScheduleTable;
import com.example.timetablebfu.GoogleSheetAPI.SheetsWork;
import com.example.timetablebfu.ViewOfTable.TimetableListAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class TimetableListFragment extends Fragment {
    private List<ScheduleTable> res;
    private SwipeRefreshLayout swipe;
    private TimetableListAdapter adapter;
    private RecyclerView recyclerView;
    private SheetsWork sheetsWork;

    private FloatingActionButton button;

    public TimetableListFragment() {
        super(R.layout.fragment_listview);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_listview, container, false);
        if (rootView != null) {
            recyclerView = rootView.findViewById(R.id.timetable_list);
            swipe = rootView.findViewById(R.id.swiperefresh);
            button = rootView.findViewById(R.id.add_floatActionBtn);
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