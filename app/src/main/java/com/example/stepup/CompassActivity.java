package com.example.stepup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import com.example.stepup.databinding.ActivityCompassBinding;
import android.widget.Button;
import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class CompassActivity extends DrawerBaseActivity {
    ActivityCompassBinding activityCompassBinding;

    //Latarka
    private Button toggleButton;
    boolean hasCameraFlash = true;
    boolean flashOn = false;

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
    }
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
}