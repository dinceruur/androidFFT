package com.github.dinceruur.androidfft.DataProcess;

import org.apache.commons.math3.stat.descriptive.moment.Mean;

public class Filters {

    /**
     * DC Component filtering
     * This methods removes the mean value of the sensor data from each element of the array.
     * @return     : filtered data
     */
    double[][] dcComponent(double[][] data){
        double[][] output = new double[data.length][data[0].length];

        // +++ DC Component Removing +++
        Mean mean = new Mean();

        for (int row = 0 ; row < data.length; row++){
            for (int column = 0; column < data[0].length ; column++){
                output[row][column] = data[row][column] - mean.evaluate(data[row]);
            }
        }
        return  output;
    }
}