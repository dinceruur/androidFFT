package com.github.dinceruur.androidfft.DataProcess;

import com.github.dinceruur.androidfft.SplashScreen;

public class SamplingRate {

    private SplashScreen splashScreen;

    private static long[]           timeStamps;
    private static int              sampleSize;
    private static double           sampleTime;
    private static int              samplingFreq;
    private final static double     TIME_CONSTANT = 1000000000d;

    public SamplingRate(SplashScreen s){
        this.splashScreen    = s;
    }

    public void init(){
        timeStamps          = splashScreen.timeStamp;
        sampleSize          = timeStamps.length;
        sampleTime          = calculateCaptureTime();
        samplingFreq        = calculateFreq();
        displayData();
    }

    private static double calculateCaptureTime() {
        long startedTime    = timeStamps[0];
        long finishTime     = timeStamps[timeStamps.length - 1];

        return ( (finishTime - startedTime) / TIME_CONSTANT );
    }

    private static int calculateFreq() {
        return (int) Math.round((double) sampleSize/sampleTime);
    }

    private void displayData() {
        splashScreen.showResults(samplingFreq);
    }

}
