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
    private ImageView imageView;
    private float[] mGravity = new float[3];
    private float[] mGeomagnetic = new float[3];
    private float azimuth = 0f;
    private float currectAzimuth = 0f;
    private SensorManager mSensorManager;

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

        //kompas
        imageView = (ImageView) findViewById(R.id.image_compass);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

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
        //kompas
        mSensorManager.registerListener(this,mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(this,mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause(){
        super.onPause();
        //kompas
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        //kompas
        final float alpha = 0.97f;
        synchronized (this) {
            if(sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                mGravity[0] = alpha*mGravity[0]+(1-alpha)*sensorEvent.values[0];
                mGravity[1] = alpha*mGravity[1]+(1-alpha)*sensorEvent.values[1];
                mGravity[2] = alpha*mGravity[2]+(1-alpha)*sensorEvent.values[2];
            }

            if(sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                mGeomagnetic[0] = alpha*mGeomagnetic[0]+(1-alpha)*sensorEvent.values[0];
                mGeomagnetic[1] = alpha*mGeomagnetic[1]+(1-alpha)*sensorEvent.values[1];
                mGeomagnetic[2] = alpha*mGeomagnetic[2]+(1-alpha)*sensorEvent.values[2];
            }

            float R[] = new float[9];
            float I[] = new float[9];
            boolean success = SensorManager.getRotationMatrix(R,I,mGravity,mGeomagnetic);
            if (success) {
                float orientation[] = new float[3];
                SensorManager.getOrientation(R,orientation);
                azimuth = (float)Math.toDegrees(orientation[0]);
                azimuth = (azimuth+360)%360;

                Animation anim = new RotateAnimation(-currectAzimuth,-azimuth,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
                currectAzimuth = azimuth;

                anim.setDuration(500);
                anim.setRepeatCount(0);
                anim.setFillAfter(true);

                imageView.startAnimation(anim);
            }
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void logout(View view){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this, LoginActivity.class));
    }
}