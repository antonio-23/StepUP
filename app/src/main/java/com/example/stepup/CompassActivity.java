package com.example.stepup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.stepup.databinding.ActivityCompassBinding;

public class CompassActivity extends DrawerBaseActivity {
    ActivityCompassBinding activityCompassBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_compass);
        activityCompassBinding = ActivityCompassBinding.inflate(getLayoutInflater());
        setContentView(activityCompassBinding.getRoot());
        allocateActivityTitle("Kompas");
    }
}