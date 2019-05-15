package com.github.dinceruur.androidfft;


import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import com.github.dinceruur.androidfft.InterFace.InterFaceDataListener;

public class AccelerometerListener implements SensorEventListener{

    private InterFaceDataListener interFaceDataListener;

    private boolean isActive = false;

    private int  sensorType;

    private long timeStamp;

    private float[] accelerometerVector = new float[3];

    public AccelerometerListener(InterFaceDataListener interFaceDataListener) {
        this.interFaceDataListener = interFaceDataListener;
    }

    public void init(){
        isActive = true;
    }

    public int getSensorType() {
        return sensorType;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public float[] getAccelerometerVector() {
        return accelerometerVector;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(isActive){
            timeStamp = event.timestamp;
            sensorType = event.sensor.getType();
            switch(sensorType) {
                case Sensor.TYPE_ACCELEROMETER:
                    System.arraycopy(event.values, 0, accelerometerVector, 0, 3);
                    break;
            }
            interFaceDataListener.notifySensorChanged();

        }
    }
}


