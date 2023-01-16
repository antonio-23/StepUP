package com.example.stepup;

import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;

public interface IBaseGpsListener extends LocationListener, GpsStatus.Listener {

    public void onLocationChanged(Location location);
}