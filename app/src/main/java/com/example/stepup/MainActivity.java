package com.example.stepup;


import androidx.appcompat.app.AppCompatDelegate;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.stepup.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends DrawerBaseActivity implements SensorEventListener {

    ActivityMainBinding activityMainBinding;

    private FirebaseAuth mAuth;
    private Button btnLogout;
    //kroki
    private SensorManager sensorManager;
    private Sensor stepCounterSensor;
    private int stepCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        btnLogout = findViewById(R.id.btnLogout);

        //kroki
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
    }

    //kroki
    public int onStartCommand(Intent intent, int flags, int startId) {
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        sensorManager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_NORMAL);

        return Service.START_STICKY;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // Sprawdź, czy wartość akceleracji w osi z przekroczyła pewien próg
        if (event.values[2] > 5.0) {
            // Jeśli tak, zwiększ licznik kroków o jeden
            stepCount++;
        }
        TextView stepCountTextView = findViewById(R.id.tv_stepsTaken); // Pobierz element TextView z interfejsu użytkownika
        stepCountTextView.setText(String.valueOf(stepCount)); // Ustaw tekst elementu TextView na aktualną liczbę kroków
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Ta metoda jest wymagana przez interfejs SensorEventListener, ale nie jest nam potrzebna w tym przypadku
    }

    public int getStepCount() {
        return stepCount;
    }

    public void logout(View view){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this, LoginActivity.class));
    }
}