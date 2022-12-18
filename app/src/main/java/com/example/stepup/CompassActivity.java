package com.example.stepup;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import com.example.stepup.databinding.ActivityCompassBinding;
import com.google.firebase.auth.FirebaseAuth;

import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;


public class CompassActivity extends DrawerBaseActivity implements SensorEventListener {
    ActivityCompassBinding activityCompassBinding;

    //Latarka
    private Button toggleButton;
    boolean hasCameraFlash = true;
    boolean flashOn = false;

    //Kompas
    private SensorManager sensorManager;
    private Sensor compassSensor;
    private TextView compassView;
    private ImageView compassImage;
    private float DegreeStart = 0f;
    long lastUpdateTime = 0;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);
        activityCompassBinding = ActivityCompassBinding.inflate(getLayoutInflater());
        setContentView(activityCompassBinding.getRoot());
        allocateActivityTitle("Kompas");

        //Latarka
        toggleButton = findViewById(R.id.torch_btn);
        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasCameraFlash) {
                    if (flashOn) {
                        flashOn = false;
                        try {
                            flashLightOff();
                        } catch (CameraAccessException e) {
                            e.printStackTrace();
                        }
                    } else {
                        flashOn = true;
                        try {
                            flashLightOn();
                        } catch (CameraAccessException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    Toast.makeText(CompassActivity.this, "Brak latarki", Toast.LENGTH_LONG).show();
                }
            }
        });

        //Kompas
        compassView = (TextView) findViewById(R.id.compass_view);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        compassSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }
    //Latarka
    private void flashLightOn() throws CameraAccessException {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        assert cameraManager != null;
        String cameraId = cameraManager.getCameraIdList()[0];
        cameraManager.setTorchMode(cameraId, true);
        Toast.makeText(CompassActivity.this, "Latarka działa", Toast.LENGTH_SHORT).show();
    }

    private void flashLightOff() throws CameraAccessException {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        assert cameraManager != null;
        String cameraId = cameraManager.getCameraIdList()[0];
        cameraManager.setTorchMode(cameraId, false);
        Toast.makeText(CompassActivity.this, "Latarka wyłączona", Toast.LENGTH_SHORT).show();

    }
    //Kompas
    @Override
    protected void onResume(){
        super.onResume();
        sensorManager.registerListener((SensorEventListener) this, compassSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause(){
        super.onPause();
        sensorManager.unregisterListener((SensorEventListener) this);
    }

    @Override
    public void onSensorChanged(SensorEvent event){
//        float degree = (float) Math.toRadians(event.values[0]);
        float degree = Math.round(event.values[0]);
        compassView.setText(Float.toString(degree) + "°");

//        RotateAnimation ra = new RotateAnimation(DegreeStart, -degree, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//        ra.setFillAfter(true);
//        ra.setDuration(250);
////        compassImage.startAnimation(ra);
//        DegreeStart = -degree;
//        lastUpdateTime = System.currentTimeMillis();

    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void logout(View view){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this, LoginActivity.class));
    }
}