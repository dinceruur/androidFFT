package com.github.dinceruur.androidfft.DataProcess;

import com.github.dinceruur.androidfft.Config.Config;
import com.jjoe64.graphview.series.DataPoint;
import org.apache.commons.math3.complex.Complex;
import com.github.dinceruur.androidfft.Fragments.CollectData;

public class DoFFT {

    private CollectData             collectData;
    private MathOperators           mathOperators;
    private Filters                 filters;
    private static double[][]       accelerationRaw;
    private static long[]           timeStamps;
    private static int              sampleSize;
    private static int              sampleTime;
    private static int              samplingFreq;
    private final static double     TIME_CONSTANT = 1000000000d;

    public DoFFT(CollectData c){
        this.collectData    = c;
    }

    static {
        System.loadLibrary("kiss-fft-lib");
    }

    public void init(){
        mathOperators       = new MathOperators();
        filters             = new Filters();
        accelerationRaw     = collectData.prepareFFTData;
        timeStamps          = collectData.timeStamp;
        sampleSize          = accelerationRaw[0].length;
        sampleTime          = calculateCaptureTime();
        samplingFreq        = calculateFreq();
        displayData();
    }

    private static int calculateCaptureTime() {
        long startedTime    = timeStamps[0];
        long finishTime     = timeStamps[accelerationRaw[0].length - 1];

        return (int) Math.round( (finishTime - startedTime) / TIME_CONSTANT );
    }

    private static int calculateFreq() {
        return (int) Math.round((double) sampleSize/sampleTime);
    }

    private native Complex[] dofftr(double[] data);

    private void displayData() {

        Config config = new Config(collectData.getContext());

        double xAxis[]          = new double[sampleSize/2 + 1];
        double[][] returnedData = new double[3][(sampleSize/2) + 1];

        if(config.getDcRemoving() == 1){
            accelerationRaw = filters.dcComponent(accelerationRaw);
        }

        for (int rowIndex = 0 ; rowIndex < 3; rowIndex++){
            returnedData[rowIndex] = mathOperators.squareRootOfComplex(dofftr(accelerationRaw[rowIndex]));
        }

        DataPoint[][] graphData = new DataPoint[returnedData.length][returnedData[0].length];

        int rowIndex = 0;

        for (double[] anReturnedData : returnedData) {
            for (int j = 0; j < anReturnedData.length; j++) {
                xAxis[j] = (double) j * samplingFreq / sampleSize;
                graphData[rowIndex][j] = new DataPoint(xAxis[j], anReturnedData[j]);
            }
            rowIndex++;
        }

        collectData.showResults(graphData);
    }
}
