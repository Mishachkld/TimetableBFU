package com.example.timetablebfu;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.timetablebfu.GoogleSheetAPI.APIConfig;
import com.example.timetablebfu.GoogleSheetAPI.retrofit.APIService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        if (savedInstanceState == null)
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.timetable_list_fragment,TimetableListFragment.class,null)
                    .setReorderingAllowed(true)
                    .commit();

    }


}
