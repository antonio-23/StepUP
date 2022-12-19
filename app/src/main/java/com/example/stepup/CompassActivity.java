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

    //Kompas1
    private SensorManager sensorManager;
    private Sensor compassSensor;
    private TextView compassView;
    private ImageView compassImage;
    private float DegreeStart = 0f;
    long lastUpdateTime = 0;

    //Kompas2
//    float[] temp = new float[9];
//    float[] rotation = new float[9];
//    float[] values = new float[3];
//    private SensorManager mSensorManager;
//    private Sensor mAccelerometer, mField;
//    private TextView valueView, directionView;
//    private float[] mGravity = new float[3];
//    private float[] mMagnetic = new float[3];


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

        //Kompas1
        compassView = (TextView) findViewById(R.id.compass_view);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        compassSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        //kompas2
//        valueView = (TextView) findViewById(R.id.compass_view);
//        directionView = (TextView) findViewById(R.id.compass_value);

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
        //kompas1
        sensorManager.registerListener((SensorEventListener) this, compassSensor, SensorManager.SENSOR_DELAY_NORMAL);
        //kompas2
//        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
//        mSensorManager.registerListener(this, mField, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause(){
        super.onPause();
        //kompas1
        sensorManager.unregisterListener((SensorEventListener) this);
        //kompas2
//        mSensorManager.unregisterListener(this);
    }

    //kompas2
//    private void updateDirection(){
//        SensorManager.getRotationMatrix(temp, null, mGravity, mMagnetic);
//        SensorManager.remapCoordinateSystem(temp, SensorManager.AXIS_X, SensorManager.AXIS_Z,rotation);
//        for (int i=0; i<values.length;i++){
//            Double degrees = (values[i]*180/Math.PI);
//            values[i] = degrees.floatValue();
//        }
//        directionView.setText(getDirectionFromDegrees(values[0]));
//        valueView.setText(String.format("Azimuth: %1$1.2f, Pitch: %2$1.2f, Roll: %3$1.2f", values[0], values[1], values[2]));
//    }

//    private String getDirectionFromDegrees(float degrees){
//        if (degrees >= -22.5 && degrees < 22.5) {
//            return "N";
//        }
//        if (degrees >= 22.5 && degrees < 67.5) {
//            return "NE";
//        }
//        if (degrees >= 67.5 && degrees < 112.5) {
//            return "E";
//        }
//        if (degrees >= 112.5 && degrees < 157.5) {
//            return "SE";
//        }
//        if (degrees >= 157.5 || degrees < -157.5) {
//            return "S";
//        }
//        if (degrees >= -157.5 && degrees < -112.5) {
//            return "SW";
//        }
//        if (degrees >= -112.5 && degrees < -67.5) {
//            return "W";
//        }
//        if (degrees >= -67.5 && degrees < -22.5) {
//            return "NW";
//        }
//
//        return null;
//    }

    @Override
    public void onSensorChanged(SensorEvent event){
        //kompas1
//        float degree = (float) Math.toRadians(event.values[0]);
//        Double degree = (event.values[0] * 180 / Math.PI);
//        Double degrees = (values[i] * 180) / Math.PI
        float degree = Math.round(event.values[0]);
        compassView.setText(Double.toString(degree) + "°");

        //animacja kompasu
//        RotateAnimation ra = new RotateAnimation(DegreeStart, -degree, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//        ra.setFillAfter(true);
//        ra.setDuration(250);
////        compassImage.startAnimation(ra); to psuje
//        DegreeStart = -degree;
//        lastUpdateTime = System.currentTimeMillis();

        //kompas2
//        switch (event.sensor.getType()){
//            case Sensor.TYPE_ACCELEROMETER:
//                System.arraycopy(event.values, 0, mGravity, 0, event.values.length);
//                break;
//            case Sensor.TYPE_MAGNETIC_FIELD:
//                System.arraycopy(event.values,0,mMagnetic,0,event.values.length);
//                break;
//            default:
//                return;
//        }
//
//
//        if (mGravity != null && mMagnetic != null){
//            updateDirection();
//        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void logout(View view){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this, LoginActivity.class));
    }
}