package com.chetanya.android.c_mouse.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.chetanya.android.c_mouse.R;
import com.chetanya.android.c_mouse.utils.SettingsUtil;

import java.util.ArrayList;
import java.util.Arrays;

public class SettingsActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ListView leftFlingList = (ListView) findViewById(R.id.leftFlingList);
        ListView rightFlingList = (ListView) findViewById(R.id.rightFlingList);

        ArrayAdapter<String> swipeOptionsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, SettingsUtil.FLING_OPTIONS);

        leftFlingList.setAdapter(swipeOptionsAdapter);
        rightFlingList.setAdapter(swipeOptionsAdapter);

        leftFlingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SettingsUtil.setLeftFling(SettingsUtil.FLING_OPTIONS.get(position));

                Toast.makeText(getApplicationContext(), "Left Fling: " + SettingsUtil.FLING_OPTIONS.get(position), Toast.LENGTH_SHORT).show();

            }
        });

        rightFlingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SettingsUtil.setRightFling(SettingsUtil.FLING_OPTIONS.get(position));

                Toast.makeText(getApplicationContext(), "Right Fling: " + SettingsUtil.FLING_OPTIONS.get(position), Toast.LENGTH_SHORT).show();

            }
        });

        Toast.makeText(getApplicationContext(), "Left Fling: " + SettingsUtil.getLeftFling() +
                ", Right Fling: " + SettingsUtil.getRightFling(), Toast.LENGTH_SHORT).show();

    }
}
