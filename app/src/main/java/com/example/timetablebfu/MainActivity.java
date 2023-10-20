package com.example.timetablebfu;

import static com.google.android.gms.common.util.CollectionUtils.listOf;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        if (savedInstanceState == null)
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.timetable_list_fragment, TimetableListFragment.class, null)
                    .setReorderingAllowed(true)
                    .commit();
    }
}


