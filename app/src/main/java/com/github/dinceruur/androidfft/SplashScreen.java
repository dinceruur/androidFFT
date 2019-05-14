package com.github.dinceruur.androidfft;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import java.util.Locale;
import com.github.dinceruur.androidfft.Config.Config;
import com.github.dinceruur.androidfft.DataProcess.SamplingRate;
import com.github.dinceruur.androidfft.InterFace.InterFaceDataListener;

public class SplashScreen extends AppCompatActivity implements InterFaceDataListener {

    Config                  config;
    AccelerometerListener   fastestListener;
    SensorManager           mSensorManager;
    public long[]           timeStamp;
    public int              graph2LastXValue = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_splash);
        config          = new Config(this);

        timeStamp = new long[config.getSampleSizeSplashScreen()];

        mSensorManager                  = (SensorManager) getSystemService(SENSOR_SERVICE);
        fastestListener                 = new AccelerometerListener(this);

        mSensorManager.registerListener(fastestListener,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_FASTEST);

        fastestListener.init();
    }

    @Override
    public void notifySensorChanged() {
        if(fastestListener.getSensorType() == Sensor.TYPE_ACCELEROMETER){
            if(graph2LastXValue < config.getSampleSizeSplashScreen()) {
                long sensorTimeStamp = fastestListener.getTimeStamp();
                timeStamp[graph2LastXValue] = sensorTimeStamp;
                graph2LastXValue++;
            }else{
                mSensorManager.unregisterListener(fastestListener);
                SamplingRate samplingRate = new SamplingRate(this);
                samplingRate.init();
            }
        }
    }

    public void showResults(int freq) {
        TextView textView = findViewById(R.id.textView);
        textView.setText(String.format(Locale.getDefault(),"Sampling Rate : %d Hz", freq));

        int SPLASH_DISPLAY_LENGTH = 2000;
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent mainIntent = new Intent(SplashScreen.this, MainActivity.class);
                SplashScreen.this.startActivity(mainIntent);
                SplashScreen.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
